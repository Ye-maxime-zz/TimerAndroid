package com.example.timeractivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.timeractivity.util.PrefUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_timer.*

class MainActivity : AppCompatActivity() {

    enum class TimerState {
        Stopped, Paused, Running

    }

    private lateinit var timer: CountDownTimer
    private var timerState = TimerState.Stopped
    private var secondsRemaining: Long = 0
    private var timerLengthSeconds: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initView()
    }

    override fun onResume() {
        super.onResume()
        initTimer()
    }

    override fun onPause() {
        super.onPause()
        if (timerState == TimerState.Running) {
            timer.cancel()
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initView() {
        fab_start.setOnClickListener {
            startTimer()
            updateButtons()
        }

        fab_pause.setOnClickListener {
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        fab_stop.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }
    }

    private fun startTimer() {
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped
        setNewTimerLength()
        progress_countdown.progress = 0
        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds
        updateButtons()
        updateCountdownUI()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = (secondsRemaining - minutesUntilFinished * 60).toString()
        textView_countdown.text =
            "$minutesUntilFinished:${if (secondsInMinuteUntilFinished.length == 2) secondsInMinuteUntilFinished else "0" + secondsInMinuteUntilFinished}"
        progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = lengthInMinutes * 60L
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.Running -> {
                fab_start.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
            }
            TimerState.Stopped -> {
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false
            }
            TimerState.Paused -> {
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
        }
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(this)
        when (timerState) {
            TimerState.Running -> {
                handlePreviousTimer()
                startTimer()
            }
            TimerState.Paused -> {
                handlePreviousTimer()
            }
            TimerState.Stopped -> {
                setNewTimerLength()
                secondsRemaining = timerLengthSeconds
            }
        }
        updateButtons()
        updateCountdownUI()
    }

    private fun handlePreviousTimer() {
        setPreviousTimerLength()
        secondsRemaining = PrefUtil.getSecondsRemaining(this)
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        progress_countdown.max = timerLengthSeconds.toInt()
    }
}
