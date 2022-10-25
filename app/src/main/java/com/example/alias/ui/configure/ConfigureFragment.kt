package com.example.alias.ui.configure

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.alias.R
import com.example.alias.databinding.ConfigureFragmentBinding
import com.example.alias.extensions.animatePagerTransition
import com.example.alias.extensions.safeNavigate
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

    private var hasFilledAllFields = false

    override fun init() {
        initViewPager()
        initObservers()
    }

    private fun initViewPager() = with(binding) {
        val fragments = arrayOf<Fragment>(
            PagerGameModeFragment(),
            PagerTeamsFragment(),
            PagerTimeAndPointsFragment()
        )
        adapter = ConfigurationViewPagerAdapter(
            fragments,
            childFragmentManager,
            viewLifecycleOwner.lifecycle,
        )
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setViewPagerCurrentItem(position)
            }
        })
    }

    private fun initObservers() = with(viewModel) {
        gameMode.observe(viewLifecycleOwner) {
            it.isClassic?.let { _ ->
                if (binding.viewPager.currentItem == 0) {
                    binding.viewPager.animatePagerTransition(400)
                }
            }

            hasFilledAllFields = it.isClassic != null && it.teams != null &&
                    isTeamsInputValid.value!! &&
                    it.timePerRound != null && it.pointsToWin != null


            if (binding.viewPager.currentItem == 2 && hasFilledAllFields)
                initBtnConfigureDone(getString(R.string.start)) {
                    findNavController().safeNavigate(getAction())
                }
            else if (binding.viewPager.currentItem == 2)
                hideBtnConfigureDone()

        }

        viewPagerCurrentItem.observe(viewLifecycleOwner) {
            when (it) {
                2 -> {
                    if (hasFilledAllFields) {
                        initBtnConfigureDone(getString(R.string.start)) {
                            findNavController().safeNavigate(getAction())
                        }
                    } else hideBtnConfigureDone()
                }
                1 -> initBtnConfigureDone(EMPTY_STRING) {
                    binding.viewPager.animatePagerTransition(400)
                }
                else -> hideBtnConfigureDone()
            }
        }
    }

    private inline fun initBtnConfigureDone(text: String, crossinline onClick: () -> Unit) =
        with(binding.btnConfigureDone) {
            setText(text)
            if (!isVisible)
                initBtnEntranceAnimation()

            setOnClickListener {
                onClick()
            }
        }

    private fun initBtnEntranceAnimation() = with(binding.btnConfigureDone) {
        alpha = 0f
        isVisible = true
        setDrawable(R.drawable.ic_arrow_right)
        animate().alpha(1f)
    }

    private fun hideBtnConfigureDone() = with(binding.btnConfigureDone) {
        animate().alpha(0f)
        isVisible = false
    }

    private fun getAction() =
        if (viewModel.gameMode.value!!.isClassic!!)
            ConfigureFragmentDirections.actionConfigureFragmentToClassicFragment(
                viewModel.gameMode.value!!
            )
        else ConfigureFragmentDirections.actionConfigureFragmentToArcadeFragment(
            viewModel.gameMode.value!!
        )

    companion object {
        private const val EMPTY_STRING = ""
    }
}
