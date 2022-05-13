package com.example.alias.ui.configure.view_pager_fragments.pager_time_and_points

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

    override fun init() {
        binding.btnDone.setOnClickListener {
            val timePerRound = binding.timeET.text.toString()
            val pointsToWin = binding.pointET.text.toString()
            if (timePerRound.isEmpty() || pointsToWin.isEmpty())
                makeToastMessage(getString(R.string.fillAllFields))
            else {
                viewModel.setTimePerRoundAndPointsToWin(
                    timePerRound = timePerRound.toInt(),
                    pointsToWin = pointsToWin.toInt()
                )
                viewModel.initGameMode()
            }
        }
    }
}
