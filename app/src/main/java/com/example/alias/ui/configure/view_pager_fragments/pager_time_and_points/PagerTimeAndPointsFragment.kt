package com.example.alias.ui.configure.view_pager_fragments.pager_time_and_points

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.fragment.app.viewModels
import com.example.alias.R
import com.example.alias.databinding.FragmentPagerTimeAndPointsBinding
import com.example.alias.ui.configure.view_pager_fragments.base.BaseFragment
import com.example.alias.ui.configure.vm.ConfigureViewModel
import me.tankery.lib.circularseekbar.CircularSeekBar

class PagerTimeAndPointsFragment :
    BaseFragment<FragmentPagerTimeAndPointsBinding>(FragmentPagerTimeAndPointsBinding::inflate) {

    private lateinit var vibratorManager: VibratorManager
    private lateinit var vibrator: Vibrator

    private val viewModel: ConfigureViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var isToastHandled = false

    override fun init() {
        initVibrator()
        initTimeSeekBar()
        initPointSeekbar()
        initOnClickListeners()
        initObservers()
    }

    private fun initVibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            vibratorManager =
                context?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        else
            vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private fun initPointSeekbar() {
        val pointSeekBar = binding.pointSeekBar
        val pointTV = binding.pointTV
        pointSeekBar.progress =
            (viewModel.gameMode.value!!.pointsToWin ?: DEFAULT_POINTS_TO_WIN).toFloat()
        pointTV.text = pointSeekBar.progress.toInt().toString()
        viewModel.setPointsToWin(DEFAULT_POINTS_TO_WIN)
        pointSeekBar.setOnSeekBarChangeListener(
            object : CircularSeekBar.OnCircularSeekBarChangeListener {
                override fun onProgressChanged(
                    circularSeekBar: CircularSeekBar?,
                    progress: Float,
                    fromUser: Boolean
                ) {
                    val progressToShow = progress - (progress % 5)
                    pointSeekBar.progress = progressToShow
                    showProgressOnTV(progressToShow.toInt())
                }

                override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {}

                override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
                    if (seekBar != null) {
                        if (seekBar.progress.toInt() < 5) {
                            seekBar.progress = 5F
                            showProgressOnTV(5)
                        }
                        viewModel.setPointsToWin(pointTV.text.toString().toInt())
                        vibrateOnStopTouch(20L)
                    }
                }

                fun showProgressOnTV(progress: Int) {
                    pointTV.text = progress.toString()
                }
            }
        )
    }

    private fun initTimeSeekBar() {
        val timeSeekBar = binding.timeSeekBar
        val timeTV = binding.timeTV
        timeSeekBar.progress =
            (viewModel.gameMode.value!!.timePerRound ?: DEFAULT_TIME_PER_ROUND).toFloat()
        timeTV.text = timeSeekBar.progress.toInt().toString()
        viewModel.setTimePerRound(DEFAULT_TIME_PER_ROUND)
        timeSeekBar.setOnSeekBarChangeListener(
            object : CircularSeekBar.OnCircularSeekBarChangeListener {
                override fun onProgressChanged(
                    circularSeekBar: CircularSeekBar?,
                    progress: Float,
                    fromUser: Boolean
                ) {
                    val progressToShow = progress - (progress % 5)
                    timeSeekBar.progress = progressToShow
                    showProgressOnTV(progressToShow.toInt())
                }

                override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {}

                override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
                    if (seekBar != null) {
                        if (seekBar.progress.toInt() < 5) {
                            seekBar.progress = 5F
                            showProgressOnTV(5)
                        }

                        viewModel.setTimePerRound(timeTV.text.toString().toInt())
                        vibrateOnStopTouch(20L)
                    }
                }

                fun showProgressOnTV(progress: Int) {
                    timeTV.text = progress.toString()
                }
            }
        )
    }

    fun vibrateOnStopTouch(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            vibratorManager.defaultVibrator.vibrate(
                VibrationEffect.createOneShot(
                    duration, VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        else
            vibrator.vibrate(duration)
    }

    private fun initObservers() {
        viewModel.gameMode.observe(viewLifecycleOwner) {
            if (it.timePerRound != null && it.pointsToWin != null && !isToastHandled) {
                if (it.isClassic == null) {
                    makeToastMessage(getString(R.string.choose_game_mode))
                    isToastHandled = true
                }
            }

            it.isClassic?.let { isClassic ->
                if (isClassic) {
                    binding.btnClassic.setBackgroundResource(R.drawable.game_mode_chosen_btn_shape_inversed)
                    binding.btnArcade.setBackgroundResource(R.drawable.game_mode_btn_shape_inversed)
                } else {
                    binding.btnClassic.setBackgroundResource(R.drawable.game_mode_btn_shape_inversed)
                    binding.btnArcade.setBackgroundResource(R.drawable.game_mode_chosen_btn_shape_inversed)
                }
            }
        }
    }

    private fun initOnClickListeners() = with(binding) {
        btnClassic.setOnClickListener { viewModel.setIsClassic(true) }
        btnArcade.setOnClickListener { viewModel.setIsClassic(false) }
    }

    companion object {
        private const val DEFAULT_TIME_PER_ROUND = 60
        private const val DEFAULT_POINTS_TO_WIN = 35
    }
}
