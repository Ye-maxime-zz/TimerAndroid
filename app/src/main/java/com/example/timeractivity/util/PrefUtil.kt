package com.example.timeractivity.util

import android.content.Context
import android.preference.PreferenceManager
import com.example.timeractivity.MainActivity

object PrefUtil {

    private const val TIMER_LENGTH_ID = "com.example.timer.timer_length"
    private const val SECONDS_REMAINING_ID = "com.example.timer.seconds_remaining"
    private const val TIMER_STATE_ID = "com.example.timer.timer_state"
    private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.example.timer.previous_timer_length_seconds"

    private fun getPreferences(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)

    fun getTimerLength(context: Context): Int {
        return getPreferences(context).getInt(TIMER_LENGTH_ID, 10)
    }

    //secondsRemaining
    fun getSecondsRemaining(context: Context): Long {
        return getPreferences(context).getLong(SECONDS_REMAINING_ID, 0L)
    }

    fun setSecondsRemaining(seconds: Long, context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().also {
            it.putLong(SECONDS_REMAINING_ID, seconds)
            it.apply()
        }
    }

    //timer state
    fun getTimerState(context: Context): MainActivity.TimerState {
        val ordinal = getPreferences(context).getInt(TIMER_STATE_ID, 0)
        return MainActivity.TimerState.values()[ordinal]
    }

    fun setTimerState(state: MainActivity.TimerState, context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().also {
            it.putInt(TIMER_STATE_ID, state.ordinal)
            it.apply()
        }
    }

    //previous timer length
    fun getPreviousTimerLengthSeconds(context: Context): Long {
        return getPreferences(context).getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
    }

    fun setPreviousTimerLengthSeconds(seconds: Long, context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().also {
            it.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            it.apply()
        }
    }
}