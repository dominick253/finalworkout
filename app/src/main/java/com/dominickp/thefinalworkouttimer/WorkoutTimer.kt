package com.dominickp.thefinalworkouttimer

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData

class WorkoutTimer(
    private val workTime: Long,
    private val restTime: Long,
    private val intervals: Int
) {
    val timerState = MutableLiveData<TimerState>()
    val intervalCount = MutableLiveData<Int>()
    private var currentInterval = 1
    private var isWorkPhase = true
    private var timer: CountDownTimer? = null

    fun start() {
        intervalCount.value = currentInterval
        timerState.value = TimerState.Work
        timer = createTimer(workTime).start()
    }

    private fun createTimer(timeInSeconds: Long): CountDownTimer {
        return object : CountDownTimer(timeInSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                if (isWorkPhase) {
                    timerState.value = TimerState.Work(timeLeft.toInt())
                } else {
                    timerState.value = TimerState.Rest(timeLeft.toInt())
                }
            }

            override fun onFinish() {
                if (isWorkPhase) {
                    if (currentInterval < intervals) {
                        isWorkPhase = false
                        timer = createTimer(restTime).start()
                    } else {
                        timerState.value = TimerState.Done
                    }
                } else {
                    currentInterval++
                    intervalCount.value = currentInterval
                    isWorkPhase = true
                    timer = createTimer(workTime).start()
                }
            }
        }
    }

    fun stop() {
        timer?.cancel()
        timerState.value = TimerState.Idle
        currentInterval = 1
        isWorkPhase = true
    }
}
