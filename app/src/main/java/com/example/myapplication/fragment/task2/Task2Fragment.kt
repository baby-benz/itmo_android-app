package com.example.myapplication.fragment.task2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentTask2Binding
import java.util.concurrent.locks.*
import kotlin.concurrent.thread
import kotlin.concurrent.withLock

class Task2Fragment : Fragment() {
    private lateinit var binding: FragmentTask2Binding

    private var fastFillSpeed: Int = FAST_FILL_SPEED
    private var slowFillSpeed: Int = SLOW_FILL_SPEED

    private val fastFillThreadLock: Lock = ReentrantLock()
    private val slowFillThreadLock: Lock = ReentrantLock()

    private val fastFillPausedCondition: Condition = fastFillThreadLock.newCondition()
    private val slowFillPausedCondition: Condition = slowFillThreadLock.newCondition()

    @Volatile
    private var isFillPaused: Boolean = false

    private var pausedByApp: Boolean = false

    @Volatile
    private var toReset: Boolean = false

    @Volatile
    private var fastFillInterval: Long = FAST_FILL_INTERVAL

    @Volatile
    private var slowFillInterval: Long = SLOW_FILL_INTERVAL

    @Volatile
    private var fastFillProgress: Int = 0

    @Volatile
    private var slowFillProgress: Int = 0

    private var fastFillThread: Thread = thread(start = false) {
        fillFastProgressBar()
    }
    private var slowFillThread: Thread = thread(start = false) {
        fillSlowProgressBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTask2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.runButton.setOnClickListener {
            toReset = false
            if (fastFillThread.state == Thread.State.NEW) {
                isFillPaused = false
                fastFillThread.start()
                slowFillThread.start()
            } else {
                resumeFilling()
            }
        }

        binding.stopButton.setOnClickListener {
            pauseFilling()
        }

        binding.resetButton.setOnClickListener {
            toReset = true
            isFillPaused = true
            fastFillThread.interrupt()
            slowFillThread.interrupt()
            binding.textViewFastSpeed.setText(R.string.task_2_fast_speed)
            binding.textViewSlowSpeed.setText(R.string.task_2_slow_speed)
        }

        binding.buttonFastSpeedUp.setOnClickListener {
            if (fastFillInterval > MINIMAL_FILL_INTERVAL) {
                fastFillInterval -= 100L
                fastFillSpeed++
                binding.textViewFastSpeed.text = fastFillSpeed.toString()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.task_2_max_speed_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.buttonFastSlowDown.setOnClickListener {
            if (fastFillInterval < MAXIMAL_FILL_INTERVAL) {
                fastFillInterval += 100L
                fastFillSpeed--
                binding.textViewFastSpeed.text = fastFillSpeed.toString()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.task_2_min_speed_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.buttonSlowSpeedUp.setOnClickListener {
            if (slowFillInterval > MINIMAL_FILL_INTERVAL) {
                slowFillInterval -= 100L
                slowFillSpeed++
                binding.textViewSlowSpeed.text = slowFillSpeed.toString()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.task_2_max_speed_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.buttonSlowSlowDown.setOnClickListener {
            if (slowFillInterval < MAXIMAL_FILL_INTERVAL) {
                slowFillInterval += 100L
                slowFillSpeed--
                binding.textViewSlowSpeed.text = slowFillSpeed.toString()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.task_2_min_speed_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fillFastProgressBar() {
        while (fastFillProgress < 100) {
            fastFillThreadLock.withLock {
                while (isFillPaused) {
                    try {
                        fastFillPausedCondition.await()
                    } catch (e: InterruptedException) {
                        if (toReset) {
                            resetFastProgress()
                            continue
                        }
                        return
                    }
                }
            }
            fastFillProgress++
            binding.fastProgressBar.progress = fastFillProgress.toFloat()
            try {
                Thread.sleep(fastFillInterval)
            } catch (e: InterruptedException) {
                if (toReset) {
                    resetFastProgress()
                    continue
                }
                return
            }
        }
        while (fastFillProgress > 0) {
            fastFillThreadLock.withLock {
                while (isFillPaused) {
                    try {
                        fastFillPausedCondition.await()
                    } catch (e: InterruptedException) {
                        if (toReset) {
                            resetFastProgress()
                            continue
                        }
                        return
                    }
                }
            }
            fastFillProgress--
            binding.fastProgressBar.progress = fastFillProgress.toFloat()
            try {
                Thread.sleep(fastFillInterval)
            } catch (e: InterruptedException) {
                if (toReset) {
                    resetFastProgress()
                    continue
                }
                return
            }
        }
        fillFastProgressBar()
    }

    private fun fillSlowProgressBar() {
        while (slowFillProgress < 100) {
            slowFillThreadLock.withLock {
                while (isFillPaused) {
                    try {
                        slowFillPausedCondition.await()
                    } catch (e: InterruptedException) {
                        if (toReset) {
                            resetSlowProgress()
                            continue
                        }
                        return
                    }
                }
            }
            slowFillProgress++
            binding.slowProgressBar.progress = slowFillProgress.toFloat()
            try {
                Thread.sleep(slowFillInterval)
            } catch (e: InterruptedException) {
                if (toReset) {
                    resetSlowProgress()
                    continue
                }
                return
            }
        }
        while (slowFillProgress > 0) {
            slowFillThreadLock.withLock {
                while (isFillPaused) {
                    try {
                        slowFillPausedCondition.await()
                    } catch (e: InterruptedException) {
                        if (toReset) {
                            resetSlowProgress()
                            continue
                        }
                        return
                    }
                }
            }
            slowFillProgress--
            binding.slowProgressBar.progress = slowFillProgress.toFloat()
            try {
                Thread.sleep(slowFillInterval)
            } catch (e: InterruptedException) {
                if (toReset) {
                    resetSlowProgress()
                    continue
                }
                return
            }
        }
        fillSlowProgressBar()
    }

    override fun onResume() {
        super.onResume()
        if (pausedByApp) {
            resumeFilling()
            pausedByApp = false
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isFillPaused) {
            pauseFilling()
            pausedByApp = true
        }
    }

    override fun onDestroy() {
        toReset = false
        fastFillThread.interrupt()
        slowFillThread.interrupt()
        super.onDestroy()
    }

    private fun resetFastProgress() {
        fastFillInterval = FAST_FILL_INTERVAL
        fastFillProgress = 0
        binding.fastProgressBar.progress = fastFillProgress.toFloat()
        fastFillSpeed = FAST_FILL_SPEED
    }

    private fun resetSlowProgress() {
        slowFillInterval = SLOW_FILL_INTERVAL
        slowFillProgress = 0
        binding.slowProgressBar.progress = slowFillProgress.toFloat()
        slowFillSpeed = SLOW_FILL_SPEED
    }

    private fun pauseFilling() {
        fastFillThreadLock.withLock {
            slowFillThreadLock.withLock {
                isFillPaused = true
            }
        }
    }

    private fun resumeFilling() {
        if (isFillPaused) {
            fastFillThreadLock.withLock {
                slowFillThreadLock.withLock {
                    isFillPaused = false
                    fastFillPausedCondition.signalAll()
                    slowFillPausedCondition.signalAll()
                }
            }
        }
    }

    companion object INTERVALS {
        private const val FAST_FILL_INTERVAL: Long = 400L
        private const val SLOW_FILL_INTERVAL: Long = 800L
        private const val MINIMAL_FILL_INTERVAL: Long = 300L
        private const val MAXIMAL_FILL_INTERVAL: Long = 1200L
        private const val FAST_FILL_SPEED: Int = 9
        private const val SLOW_FILL_SPEED: Int = 5
    }
}