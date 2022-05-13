package com.example.alias.ui.configure.view_pager_fragments.pager_game_mode

import androidx.fragment.app.viewModels
import com.example.alias.databinding.FragmentPagerGameModeBinding
import com.example.alias.ui.configure.view_pager_fragments.base.BaseFragment
import com.example.alias.ui.configure.vm.ConfigureViewModel

class PagerGameModeFragment :
    BaseFragment<FragmentPagerGameModeBinding>(FragmentPagerGameModeBinding::inflate) {

    private val viewModel: ConfigureViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun init() {
        binding.classic.setOnClickListener {
            viewModel.setIsClassic(true)
        }

        binding.arcade.setOnClickListener {
            viewModel.setIsClassic(false)
        }
    }

}
