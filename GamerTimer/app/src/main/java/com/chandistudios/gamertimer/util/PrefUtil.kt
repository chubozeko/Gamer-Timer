package com.chandistudios.gamertimer.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.chandistudios.gamertimer.MainActivity
import java.util.prefs.Preferences
import java.util.prefs.PreferencesFactory

class PrefUtil {
    companion object {

        private const val TIMER_LENGTH_ID = "com.chandistudios.gamertimer.timer_length"
        fun getTimerLength(context: Context): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(TIMER_LENGTH_ID, 20)
        }

        private const val REST_TIMER_LENGTH_ID = "com.chandistudios.gamertimer.rest_timer_length"
        fun getRestTimerLength(context: Context): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(REST_TIMER_LENGTH_ID, 5)
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.chandistudios.timer.previous_timer_length"
        fun getPreviousTimerLengthSeconds(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }
        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private const val PREVIOUS_REST_TIMER_LENGTH_SECONDS_ID = "com.chandistudios.timer.previous_rest_timer_length"
        fun getPreviousRestTimerLengthSeconds(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_REST_TIMER_LENGTH_SECONDS_ID, 0)
        }
        fun setPreviousRestTimerLengthSeconds(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_REST_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "com.chandistudios.timer.timer_state"
        fun getTimerState(context: Context): MainActivity.TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return MainActivity.TimerState.values()[ordinal]
        }
        fun setTimerState(state: MainActivity.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        private const val TIMER_TYPE_ID = "com.chandistudios.timer.timer_type"
        fun getTimerType(context: Context): MainActivity.TimerType {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_TYPE_ID, 0)
            return MainActivity.TimerType.values()[ordinal]
        }
        fun setTimerType(state: MainActivity.TimerType, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_TYPE_ID, ordinal)
            editor.apply()
        }

        private const val SECONDS_REMAINING_ID = "com.chandistudios.timer.seconds_remaining"
        fun getSecondsRemaining(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }
        fun setSecondsRemaining(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "com.chandistudios.timer.background_time"
        fun getAlarmSetTime(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID, 0)
        }
        fun setAlarmSetTime(time: Long, context: Context){
            val editor= PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}