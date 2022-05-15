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
        binding.timeET.addTextChangedListener {
            if (!it.isNullOrEmpty())
                viewModel.setTimePerRound(it.toString().toInt())
            else viewModel.setTimePerRound(null)
        }
        binding.pointET.addTextChangedListener {
            if (!it.isNullOrEmpty())
                viewModel.setPointsToWin(it.toString().toInt())
            else viewModel.setPointsToWin(null)
        }

        viewModel.gameMode.observe(viewLifecycleOwner) {
            if (it.timePerRound != null && it.pointsToWin != null && !isToastHandled) {
                if (it.isClassic == null) {
                    makeToastMessage("choose game mode")
                    isToastHandled = true
                }
            }
        }
    }
}
