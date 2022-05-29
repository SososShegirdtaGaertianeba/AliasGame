package com.example.alias.ui.configure

import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    private var hasFilledAllFields = false
    private var currentIsClassic: Boolean? = null

    override fun init() {
        initViewPager()
        initObservers()
    }

    private fun getAction() =
        if (viewModel.gameMode.value!!.isClassic!!)
            ConfigureFragmentDirections.actionConfigureFragmentToClassicFragment(
                viewModel.gameMode.value!!
            )
        else
            ConfigureFragmentDirections.actionConfigureFragmentToArcadeFragment(
                viewModel.gameMode.value!!
            )

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
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setViewPagerCurrentItem(position)
            }
        })
    }

    private fun initObservers() {
        viewModel.gameMode.observe(viewLifecycleOwner) {
            it.isClassic?.let { _ ->
                if (it.isClassic != currentIsClassic && binding.viewPager.currentItem == 0) {
                    currentIsClassic = it.isClassic
                    binding.viewPager.animatePagerTransition(400)
                }
            }
            hasFilledAllFields = it.isClassic != null && it.teams != null &&
                    viewModel.isTeamsInputValid.value!! &&
                    it.timePerRound != null && it.pointsToWin != null
        }

        viewModel.viewPagerCurrentItem.observe(viewLifecycleOwner) {
            when (it) {
                2 -> {
                    if (hasFilledAllFields) {
                        binding.btnConfigureDone.setText(getString(R.string.start))
                        if (!binding.btnConfigureDone.isVisible)
                            initBtnConfigureDone()
                        binding.btnConfigureDone.setOnClickListener {
                            binding.btnConfigureDone.animate().alpha(0f)
                            findNavController().navigate(getAction())
                        }
                    } else hideBtnConfigureDone()
                }
                1 -> {
                    binding.btnConfigureDone.setText(getString(R.string.next))
                    if (!binding.btnConfigureDone.isVisible)
                        initBtnConfigureDone()
                    binding.btnConfigureDone.setOnClickListener {
                        binding.viewPager.animatePagerTransition(400)
                    }
                }
                else -> hideBtnConfigureDone()
            }
        }
    }

    private fun initBtnConfigureDone() = with(binding) {
        btnConfigureDone.alpha = 0f
        btnConfigureDone.isVisible = true
        btnConfigureDone.setDrawable(R.drawable.ic_arrow_right)
        btnConfigureDone.animate().alpha(1f)
    }

    private fun hideBtnConfigureDone() = with(binding) {
        btnConfigureDone.animate().alpha(0f)
        btnConfigureDone.isVisible = false
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
