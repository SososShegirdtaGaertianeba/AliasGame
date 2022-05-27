package com.example.alias.ui.configure

import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.alias.R
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
        binding.viewPager
    }

    private fun initObservers() {
        viewModel.gameMode.observe(viewLifecycleOwner) {
            it.isClassic?.let { _ ->
                if (it.teams == null)
                    binding.viewPager.animatePagerTransition(400)
            }

            if (
                it.isClassic != null && it.teams != null &&
                viewModel.isTeamsInputValid.value!! &&
                it.timePerRound != null && it.pointsToWin != null
            ) {
                val action =
                    if (viewModel.gameMode.value!!.isClassic!!)
                        ConfigureFragmentDirections.actionConfigureFragmentToClassicFragment(
                            viewModel.gameMode.value!!
                        )
                    else
                        ConfigureFragmentDirections.actionConfigureFragmentToArcadeFragment(
                            viewModel.gameMode.value!!
                        )

                initBtnConfigureDone(action)
            } else binding.btnConfigureDone.isVisible = false
        }
    }

    private fun initBtnConfigureDone(action: NavDirections) = with(binding) {
        btnConfigureDone.alpha = 0f
        btnConfigureDone.isVisible = true
        btnConfigureDone.setText(getString(R.string.start))
        btnConfigureDone.setDrawable(R.drawable.ic_arrow_right)
        btnConfigureDone.animate().alpha(1f)
        btnConfigureDone.setOnClickListener {
            findNavController().navigate(action)
        }

    }

    // ViewPager Transition With Custom Duration
    private fun ViewPager2.animatePagerTransition(transitionDuration: Long) {
        val animator = ValueAnimator.ofInt(0, width)

        animator.addListener(
            onStart = {},
            onEnd = { endFakeDrag() },
            onCancel = { endFakeDrag() },
            onRepeat = {}
        )

        animator.interpolator = AccelerateInterpolator()

        var oldDragPosition = 0f
        animator.addUpdateListener {
            val dragPosition = (it.animatedValue as Int).toFloat()
            val dragOffset = dragPosition - oldDragPosition
            oldDragPosition = dragPosition
            fakeDragBy(-dragOffset)
        }

        animator.duration = transitionDuration
        if (beginFakeDrag())
            animator.start()
    }
}
