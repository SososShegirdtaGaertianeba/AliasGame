package com.example.alias.ui.configure.view_pager_fragments.pager_time_and_points

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
        binding.tvTime.addTextChangedListener {
            if (!it.isNullOrEmpty())
                viewModel.setTimePerRound(it.toString().toInt())
            else viewModel.setTimePerRound(null)
        }
        binding.tvPoints.addTextChangedListener {
            if (!it.isNullOrEmpty())
                viewModel.setPointsToWin(it.toString().toInt())
            else viewModel.setPointsToWin(null)
        }

    }

    private fun initObservers() {
        viewModel.gameMode.observe(viewLifecycleOwner) {
            if (it.timePerRound!! > 0 && it.pointsToWin!! > 0 && !isToastHandled) {
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
    }

    private fun initOnClickListeners() = with(binding) {
        btnClassic.setOnClickListener { viewModel.setIsClassic(true) }
        btnArcade.setOnClickListener { viewModel.setIsClassic(false) }

        btnIncreaseTime.setOnClickListener {
            val time = tvTime.text.toString().toInt()
            val increased = time + 5
            tvTime.text = increased.toString()
        }

        btnDecreaseTime.setOnClickListener {
            val time = tvTime.text.toString().toInt()
            if (time > 5) {
                val decreased = time - 5
                tvTime.text = decreased.toString()
            } else
                makeToastMessage(TIME_NON_POSITIVE_MESSAGE)
        }

        btnIncreasePoints.setOnClickListener {
            val points = tvPoints.text.toString().toInt()
            val increased = points + 5
            tvPoints.text = increased.toString()
        }

        btnDecreasePoints.setOnClickListener {
            val points = tvPoints.text.toString().toInt()
            if (points > 5) {
                val decreased = points - 5
                tvPoints.text = decreased.toString()
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
