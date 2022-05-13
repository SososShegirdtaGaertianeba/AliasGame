package com.example.alias.ui.configure.view_pager_fragments.pager_teams

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alias.R
import com.example.alias.databinding.FragmentPagerTeamsBinding
import com.example.alias.ui.configure.view_pager_fragments.base.BaseFragment
import com.example.alias.ui.configure.view_pager_fragments.pager_teams.adapter.TeamsAdapter
import com.example.alias.ui.configure.vm.ConfigureViewModel

class PagerTeamsFragment :
    BaseFragment<FragmentPagerTeamsBinding>(FragmentPagerTeamsBinding::inflate) {
    private val adapter by lazy { TeamsAdapter() }

    private val viewModel: ConfigureViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun init() =
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            btnAddTeam.setOnClickListener {
                if (adapter.teams.size < MAX_TEAMS)
                    adapter.addTeam()
                else
                    makeToastMessage(getString(R.string.max6Teams))
            }

            btnDeleteLast.setOnClickListener {
                if (adapter.teams.size > MIN_TEAMS) {
                    adapter.teams.removeLast()
                    adapter.notifyItemRemoved(adapter.teams.size)
                } else makeToastMessage(getString(R.string.min2Teams))
            }

            btnDone.setOnClickListener {
                if (adapter.teams.toSet().size != adapter.teams.size)
                    makeToastMessage(getString(R.string.uniqueConstraint))
                else
                    viewModel.setTeams(adapter.teams)
            }
        }

    companion object {
        private const val MAX_TEAMS = 6
        private const val MIN_TEAMS = 2
    }
}
