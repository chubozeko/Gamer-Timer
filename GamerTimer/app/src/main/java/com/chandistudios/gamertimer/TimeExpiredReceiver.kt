package com.chandistudios.gamertimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chandistudios.gamertimer.util.NotificationUtil
import com.chandistudios.gamertimer.util.PrefUtil

class TimeExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(MainActivity.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}
