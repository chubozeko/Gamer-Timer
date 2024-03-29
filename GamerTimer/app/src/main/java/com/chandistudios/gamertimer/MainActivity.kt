package com.chandistudios.gamertimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import com.chandistudios.gamertimer.util.NotificationUtil
import com.chandistudios.gamertimer.util.PrefUtil

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long {
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimeExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimeExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }

    enum class TimerState {
        Stopped, Paused, Running
    }

    enum class TimerType {
        Gaming, Resting
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.Stopped
    private var secondsRemaining = 0L
    private var timerType = TimerType.Gaming
    private var timerTypeTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setLogo(R.drawable.ic_gamepad)
        toolbar.title = "Gamer Timer"

        fab_play.setOnClickListener { v ->
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        fab_pause.setOnClickListener {v ->
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        fab_stop.setOnClickListener { v ->
            timer.cancel()
            changeTimerType()
            onTimerFinished()
        }

        fab_restart.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        if (timerType == TimerType.Gaming)
            timerTypeTitle = "GAMING TIME!"
        else
            timerTypeTitle = "time to rest..."

        tv_timerTitle.text = timerTypeTitle
    }

    override fun onResume() {
        super.onResume()

        initTimer()

        removeAlarm(this)
        NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        super.onPause()

        if(timerState == TimerState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(this, nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(this, wakeUpTime)
        } else if(timerState == TimerState.Paused){
            NotificationUtil.showTimerPaused(this)
        }

        if (timerType == TimerType.Gaming){
            PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        } else {
            PrefUtil.setPreviousRestTimerLengthSeconds(timerLengthSeconds, this)
        }
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
        PrefUtil.setTimerType(timerType, this)
    }

    private fun initTimer(){
        timerType = PrefUtil.getTimerType(this)

        timerState = PrefUtil.getTimerState(this)
        if(timerState == TimerState.Stopped) {
            if (timerType == TimerType.Gaming)
                setNewTimerLength()
            else
                setNewRestTimerLength()
        } else {
            if (timerType == TimerType.Gaming)
                setPreviousTimerLength()
            else
                setPreviousRestTimerLength()
        }

        secondsRemaining = if(timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if(alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if(secondsRemaining <= 0)
            onTimerFinished()
        else if(timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        if(timerType == TimerType.Gaming)
            setNewTimerLength()
        else
            setNewRestTimerLength()


        pb_timer.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun changeTimerType(){
        if(timerType == TimerType.Gaming) {
            timerType = TimerType.Resting
            timerTypeTitle = "GAMING TIME!"
        } else {
            timerType = TimerType.Gaming
            timerTypeTitle = "time to rest..."
        }
    }

    private fun startTimer(){
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000){
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        pb_timer.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        pb_timer.max = timerLengthSeconds.toInt()
    }

    private fun setNewRestTimerLength(){
        val lengthInMinutes = PrefUtil.getRestTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        pb_timer.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousRestTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousRestTimerLengthSeconds(this)
        pb_timer.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinutesUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinutesUntilFinished.toString()
        txt_countdown.text = "$minutesUntilFinished: ${
        if (secondsStr.length == 2) secondsStr
        else "0" + secondsStr }"
        pb_timer.progress = (timerLengthSeconds - secondsRemaining).toInt()
        if(timerType == TimerType.Gaming) {
            timerTypeTitle = "GAMING TIME!"
        } else {
            timerTypeTitle = "time to rest..."
        }
        tv_timerTitle.text = timerTypeTitle
    }

    private fun updateButtons(){
        when (timerState){
            TimerState.Running -> {
                fab_play.isEnabled = false
                fab_play.isVisible = false
                fab_pause.isEnabled = true
                fab_pause.isVisible = true
                fab_stop.isEnabled = true
            }
            TimerState.Paused -> {
                fab_play.isEnabled = true
                fab_play.isVisible = true
                fab_pause.isEnabled = false
                fab_pause.isVisible = false
                fab_stop.isEnabled = true
            }
            TimerState.Stopped -> {
                fab_play.isEnabled = true
                fab_play.isVisible = true
                fab_pause.isEnabled = false
                fab_pause.isVisible = false
                fab_stop.isEnabled = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
