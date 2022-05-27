package com.example.alias.ui.result

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alias.R
import com.example.alias.databinding.ResultFragmentBinding
import com.example.alias.extensions.safeNavigate
import com.example.alias.ui.base.BaseFragment
import com.example.alias.ui.classic.adapter.WordsAdapter
import com.example.alias.util.GameMode

class ResultFragment : BaseFragment<ResultFragmentBinding>(ResultFragmentBinding::inflate) {
    private val safeArgs: ResultFragmentArgs by navArgs()
    private lateinit var gameMode: GameMode
    private lateinit var scoreArray: IntArray
    private val teamMap = mutableMapOf<String, Int>()
    private val adapter: WordsAdapter by lazy {
        WordsAdapter({}, {})
    }

    override fun init() {
        initGameMode()
        initTeamMap()
        initRecycler()
        populateRecycler()
        initListeners()
    }

    private fun initListeners() {
        binding.btnRematch.setOnClickListener {
            val action = when (gameMode.isClassic) {
                true -> ResultFragmentDirections.actionResultFragmentToClassicFragment(gameMode)
                else -> ResultFragmentDirections.actionResultFragmentToArcadeFragment(gameMode)
            }
            findNavController().safeNavigate(action)
        }

        binding.btnConfigureGame.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_configureFragment)
        }
    }

    private fun initRecycler() {
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun populateRecycler() =
        adapter.setData(teamMap.keys.toMutableList())

    private fun initTeamMap() {
        for (i in gameMode.teams!!.indices)
            teamMap[gameMode.teams!![i]] = scoreArray[i]
    }

    private fun initGameMode() {
        gameMode = safeArgs.gameMode
        scoreArray = safeArgs.scoreArray
    }

}