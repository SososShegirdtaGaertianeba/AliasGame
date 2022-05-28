package com.example.alias.ui.configure.view_pager_fragments.pager_teams

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alias.R
import com.example.alias.databinding.FragmentPagerTeamsBinding
import com.example.alias.ui.configure.view_pager_fragments.base.BaseFragment
import com.example.alias.ui.configure.view_pager_fragments.pager_teams.adapter.TeamsAdapter
import com.example.alias.ui.configure.vm.ConfigureViewModel

class PagerTeamsFragment :
    BaseFragment<FragmentPagerTeamsBinding>(FragmentPagerTeamsBinding::inflate) {
    private val adapter by lazy {
        TeamsAdapter {
            viewModel.setTeams(it)
        }
    }
    private var constraintMessageShown = false

    private val viewModel: ConfigureViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun init() =
        with(binding) {

            // Init Recycler
            val layoutManager = LinearLayoutManager(requireContext())
            layoutManager.stackFromEnd = true
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager

            // Set Delete On Swipe
            initSwipeListener()

            initObservers()

            // Set Add On Click
            btnAddTeam.setOnClickListener {
                if (adapter.teams.size < MAX_TEAMS)
                    adapter.addTeam()
                else
                    makeToastMessage(getString(R.string.max6Teams))
            }


            btnClassic.setOnClickListener { viewModel.setIsClassic(true) }
            btnArcade.setOnClickListener { viewModel.setIsClassic(false) }
        }

    private fun initSwipeListener() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            // Restrict Move
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            // Restrict Swipe on MIN_TEAMS Condition
            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (adapter.teams.size > MIN_TEAMS) super.getSwipeDirs(
                    recyclerView,
                    viewHolder
                )
                else 0
            }

            // Delete on Swipe
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                if (adapter.teams.size > MIN_TEAMS)
                    adapter.deleteTeam(position)
                else makeToastMessage(getString(R.string.min2Teams))
            }
        }).attachToRecyclerView(binding.recyclerView)
    }

    private fun initObservers() {
        viewModel.isTeamsInputValid.observe(viewLifecycleOwner) {
            if (!it && !constraintMessageShown) {
                makeToastMessage(getString(R.string.uniqueConstraint))
                constraintMessageShown = true
            }
        }

        viewModel.gameMode.observe(viewLifecycleOwner) { gm ->
            gm.teams?.let {
                if (it.size > 3)
                    binding.drawable.animate().alpha(0f)
                else
                    binding.drawable.animate().alpha(1f)

            }

            gm.isClassic?.let {
                if (it) {
                    binding.btnClassic.setBackgroundResource(R.color.subtle_green)
                    binding.btnArcade.setBackgroundResource(R.color.dark_blue)
                } else {
                    binding.btnClassic.setBackgroundResource(R.color.dark_blue)
                    binding.btnArcade.setBackgroundResource(R.color.subtle_green)
                }
            }
        }
    }

    companion object {
        private const val MAX_TEAMS = 6
        private const val MIN_TEAMS = 2
    }
}
