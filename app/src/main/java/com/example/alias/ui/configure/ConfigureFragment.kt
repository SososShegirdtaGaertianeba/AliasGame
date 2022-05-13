package com.example.alias.ui.configure

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.alias.databinding.ConfigureFragmentBinding
import com.example.alias.ui.base.BaseFragment
import com.example.alias.ui.configure.adapter.ConfigurationViewPagerAdapter
import com.example.alias.ui.configure.view_pager_fragments.pager_game_mode.PagerGameModeFragment
import com.example.alias.ui.configure.view_pager_fragments.pager_teams.PagerTeamsFragment
import com.example.alias.ui.configure.view_pager_fragments.pager_time_and_points.PagerTimeAndPointsFragment
import com.example.alias.ui.configure.vm.ConfigureViewModel
import com.example.alias.util.PagingEvent

class ConfigureFragment :
    BaseFragment<ConfigureFragmentBinding>(ConfigureFragmentBinding::inflate) {

    private val viewModel: ConfigureViewModel by viewModels()

    private lateinit var adapter: ConfigurationViewPagerAdapter

    private val viewPager: ViewPager2
        get() = binding.viewPager

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
        viewPager.adapter = adapter

    }

    private fun initObservers() {
        var i = 0
        viewModel.events.forEach { observer ->
            observer.observe(viewLifecycleOwner) {
                if (it.isHandled) {
                    viewPager.currentItem = it.position
                    viewModel.events[i++].value = PagingEvent(it.data, it.position)
                }
            }
        }
    }

}
