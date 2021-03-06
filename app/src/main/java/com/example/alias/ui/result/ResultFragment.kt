package com.example.alias.ui.result

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alias.R
import com.example.alias.databinding.ResultFragmentBinding
import com.example.alias.extensions.safeNavigate
import com.example.alias.ui.base.BaseFragment
import com.example.alias.ui.score_break.adapter.TeamScoreBreakAdapter
import com.example.alias.util.GameMode

class ResultFragment : BaseFragment<ResultFragmentBinding>(ResultFragmentBinding::inflate) {
    private val safeArgs: ResultFragmentArgs by navArgs()
    private lateinit var gameMode: GameMode
    private lateinit var scoreArray: IntArray
    private val teamMap = mutableMapOf<String, Int>()
    private lateinit var adapter: TeamScoreBreakAdapter

    override fun init() {
        initGameMode()
        initTeamMap()
        initRecycler()
        populateRecycler()
        initButtons()
    }

    private fun initButtons() = with(binding) {
        btnRematch.setOnClickListener {
            val action = when (gameMode.isClassic) {
                true -> ResultFragmentDirections.actionResultFragmentToClassicFragment(gameMode)
                else -> ResultFragmentDirections.actionResultFragmentToArcadeFragment(gameMode)
            }
            findNavController().safeNavigate(action)
        }

        btnNewGame.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_configureFragment)
        }
    }

    private fun initRecycler() {
        with(binding) {
            adapter = TeamScoreBreakAdapter()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun populateRecycler() =
        adapter.setData(teamMap)

    private fun initTeamMap() {
        for (i in gameMode.teams!!.indices)
            teamMap[gameMode.teams!![i]] = scoreArray[i]
    }

    private fun initGameMode() {
        gameMode = safeArgs.gameMode
        scoreArray = safeArgs.scoreArray
    }

}