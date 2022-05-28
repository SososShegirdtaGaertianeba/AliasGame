package com.example.alias.ui.configure.view_pager_fragments.pager_game_mode

import androidx.fragment.app.viewModels
import com.example.alias.R
import com.example.alias.databinding.FragmentPagerGameModeBinding
import com.example.alias.ui.configure.view_pager_fragments.base.BaseFragment
import com.example.alias.ui.configure.vm.ConfigureViewModel

class PagerGameModeFragment :
    BaseFragment<FragmentPagerGameModeBinding>(FragmentPagerGameModeBinding::inflate) {

    private val viewModel: ConfigureViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun init() {
        initObservers()
        initListeners()
    }

    private fun initListeners() = with(binding) {
        binding.classic.setOnClickListener { viewModel.setIsClassic(true) }
        binding.arcade.setOnClickListener { viewModel.setIsClassic(false) }
    }

    private fun initObservers() {
        viewModel.gameMode.observe(viewLifecycleOwner) { gm ->
            gm.isClassic?.let {
                if (it) {
                    binding.classic.setBackgroundResource(R.drawable.game_mode_chosen_btn_shape)
                    binding.arcade.setBackgroundResource(R.drawable.game_mode_btn_shape)
                } else {
                    binding.arcade.setBackgroundResource(R.drawable.game_mode_chosen_btn_shape)
                    binding.classic.setBackgroundResource(R.drawable.game_mode_btn_shape)
                }
            }
        }
    }

}
