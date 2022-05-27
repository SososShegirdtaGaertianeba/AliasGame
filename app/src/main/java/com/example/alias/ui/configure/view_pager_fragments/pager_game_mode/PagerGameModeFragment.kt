package com.example.alias.ui.configure.view_pager_fragments.pager_game_mode

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.alias.R
import com.example.alias.databinding.FragmentPagerGameModeBinding
import com.example.alias.ui.configure.view_pager_fragments.base.BaseFragment
import com.example.alias.ui.configure.vm.ConfigureViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PagerGameModeFragment :
    BaseFragment<FragmentPagerGameModeBinding>(FragmentPagerGameModeBinding::inflate) {

    private val viewModel: ConfigureViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun init() {
        binding.classic.setOnClickListener {
            it.setBackgroundResource(R.drawable.game_mode_chosen_btn_shape)
            binding.arcade.setBackgroundResource(R.drawable.arcade_btn_shape)
            lifecycleScope.launch {
                viewModel.setIsClassic(true)
            }
        }

        binding.arcade.setOnClickListener {
            it.setBackgroundResource(R.drawable.game_mode_chosen_btn_shape)
            binding.classic.setBackgroundResource(R.drawable.classic_btn_shape)
            lifecycleScope.launch {
                viewModel.setIsClassic(false)
            }
        }
    }

}
