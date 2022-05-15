package com.example.alias.ui.configure

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.alias.databinding.ConfigureFragmentBinding
import com.example.alias.ui.base.BaseFragment
import com.example.alias.ui.configure.adapter.ConfigurationViewPagerAdapter
import com.example.alias.ui.configure.view_pager_fragments.pager_game_mode.PagerGameModeFragment
import com.example.alias.ui.configure.view_pager_fragments.pager_teams.PagerTeamsFragment
import com.example.alias.ui.configure.view_pager_fragments.pager_time_and_points.PagerTimeAndPointsFragment
import com.example.alias.ui.configure.vm.ConfigureViewModel

class ConfigureFragment :
    BaseFragment<ConfigureFragmentBinding>(ConfigureFragmentBinding::inflate) {

    private val viewModel: ConfigureViewModel by viewModels()

    private lateinit var adapter: ConfigurationViewPagerAdapter


    override fun init() {
        initViewPager()
        initObservers()
    }

    private fun initViewPager() {
        val fragmentList = arrayOf<Fragment>(
            PagerGameModeFragment(),
            PagerTeamsFragment(),
            PagerTimeAndPointsFragment()
        )
        adapter = ConfigurationViewPagerAdapter(
            fragmentList,
            childFragmentManager,
            viewLifecycleOwner.lifecycle,
        )
        binding.viewPager.adapter = adapter

    }

    private fun initObservers() {
        viewModel.gameMode.observe(viewLifecycleOwner) {
            it.isClassic?.let { _ ->
                if (it.teams == null)
                    binding.viewPager.currentItem = 1
            }



            if (it.isClassic != null && it.teams != null
                && it.timePerRound != null && it.pointsToWin != null
            ) {
                val action =
                    if (viewModel.gameMode.value!!.isClassic!!) ConfigureFragmentDirections.actionConfigureFragmentToClassicFragment(
                        viewModel.gameMode.value!!
                    )
                    else ConfigureFragmentDirections.actionConfigureFragmentToArcadeFragment(
                        viewModel.gameMode.value!!
                    )
                binding.btnConfigureDone.isVisible = true
                binding.btnConfigureDone.setOnClickListener {
                    findNavController().navigate(action)
                }
            } else binding.btnConfigureDone.isVisible = false
        }
    }

}
