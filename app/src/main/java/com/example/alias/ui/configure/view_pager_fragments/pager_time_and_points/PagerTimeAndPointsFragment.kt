package com.example.alias.ui.configure.view_pager_fragments.pager_time_and_points

import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.alias.R
import com.example.alias.databinding.FragmentPagerTimeAndPointsBinding
import com.example.alias.ui.configure.view_pager_fragments.base.BaseFragment
import com.example.alias.ui.configure.vm.ConfigureViewModel

class PagerTimeAndPointsFragment :
    BaseFragment<FragmentPagerTimeAndPointsBinding>(FragmentPagerTimeAndPointsBinding::inflate) {

    private val viewModel: ConfigureViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var isToastHandled = false

    override fun init() {
        initTextViews()
        initOnClickListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.gameMode.observe(viewLifecycleOwner) {
//            Log.d("TIME_POINTS", "${it.timePerRound}   ${it.pointsToWin}")

            if (it.timePerRound != null && it.pointsToWin != null && !isToastHandled) {
                if (it.isClassic == null) {
                    makeToastMessage(IS_CLASSIC_IS_NULL_MESSAGE)
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

    private fun initTextViews() = with(binding) {
        tvTime.text = (viewModel.gameMode.value!!.timePerRound ?: DEFAULT_TIME_PER_ROUND).toString()
        tvPoints.text = (viewModel.gameMode.value!!.pointsToWin ?: DEFAULT_POINTS_TO_WIN).toString()

        viewModel.setTimePerRound(tvTime.text.toString().toInt())
        viewModel.setPointsToWin(tvPoints.text.toString().toInt())
    }

    private fun initOnClickListeners() = with(binding) {
        btnClassic.setOnClickListener { viewModel.setIsClassic(true) }
        btnArcade.setOnClickListener { viewModel.setIsClassic(false) }

        btnIncreaseTime.setOnClickListener {
            val time = tvTime.text.toString().toInt()
            val increased = time + 5
            tvTime.text = increased.toString()
            viewModel.setTimePerRound(increased)
        }

        btnDecreaseTime.setOnClickListener {
            val time = tvTime.text.toString().toInt()
            if (time > 5) {
                val decreased = time - 5
                tvTime.text = decreased.toString()
                viewModel.setTimePerRound(decreased)
            } else
                makeToastMessage(TIME_NON_POSITIVE_MESSAGE)
        }

        btnIncreasePoints.setOnClickListener {
            val points = tvPoints.text.toString().toInt()
            val increased = points + 5
            tvPoints.text = increased.toString()
            viewModel.setPointsToWin(increased)
        }

        btnDecreasePoints.setOnClickListener {
            val points = tvPoints.text.toString().toInt()
            if (points > 5) {
                val decreased = points - 5
                tvPoints.text = decreased.toString()
                viewModel.setPointsToWin(decreased)
            } else
                makeToastMessage(POINTS_NON_POSITIVE_MESSAGE)
        }
    }

    companion object {
        private const val DEFAULT_TIME_PER_ROUND = 60
        private const val DEFAULT_POINTS_TO_WIN = 30

        // TMP
        private const val IS_CLASSIC_IS_NULL_MESSAGE = "Choose Game Mode"
        private const val TIME_NON_POSITIVE_MESSAGE = "Time per Round Should be Greater than Zero"
        private const val POINTS_NON_POSITIVE_MESSAGE = "Points to Win Should be Greater than Zero"
    }
}
