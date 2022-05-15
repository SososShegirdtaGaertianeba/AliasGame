package com.example.alias.ui.classic

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alias.databinding.ClassicFragmentBinding
import com.example.alias.di.viewModels
import com.example.alias.safeNavigate
import com.example.alias.ui.base.BaseFragment
import com.example.alias.ui.classic.adapter.WordsAdapter
import com.example.alias.ui.classic.vm.ClassicViewModel
import com.example.alias.util.GameMode
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class ClassicFragment : BaseFragment<ClassicFragmentBinding>(ClassicFragmentBinding::inflate) {
    private lateinit var getWords: suspend () -> MutableList<String>
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var recyclerView: RecyclerView
    private lateinit var gameMode: GameMode
    private var isGameFinished = false

    private val safeArgs: ClassicFragmentArgs by navArgs()
    private val classicViewModel: ClassicViewModel by viewModel()

    private val wordsAdapter by lazy {
        WordsAdapter(
            { classicViewModel.incrementScore() },
            { classicViewModel.decrementScore() }
        )
    }

    private var timePerRound = 0
    private var prevCompletionPoints = 0

    private val language = fun(context: Context) =
        (context.getSharedPreferences("languageSharedPreference", Context.MODE_PRIVATE))
            .getInt("language", 0)

    override fun init() {
        loadKoinModules(viewModels)
        initGameMode()
        initRecycler()
        initObservers()
        initWordGetter(requireContext())
        makeRequest()
        binding.tvCurrentTeam.text = classicViewModel.currentTeam
        initCountDown(timePerRound)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
        unloadKoinModules(viewModels)
    }

    private fun initCountDown(timePerRound: Int) {
        countDownTimer = object : CountDownTimer(timePerRound * 1000L, 1000) {
            override fun onTick(timeLeft: Long) {
                val toDisplay = timeLeft / 1000 + 1
                binding.tvCountDown.text = toDisplay.toString()
            }

            override fun onFinish() {
                classicViewModel.saveCurrentTeamScore()
                if (!isGameFinished)
                    navigateToScoreBreak()
                else
                    navigateToResultFragment()
            }
        }
        countDownTimer.start()
    }

    private fun navigateToResultFragment() {
        val action = ClassicFragmentDirections.actionClassicFragmentToResultFragment(
            gameMode,
            classicViewModel.teams.values.toIntArray()
        )
        findNavController().navigate(action)
    }

    private fun startNextTeamRound() {
        classicViewModel.startNextTeamRound()
        prevCompletionPoints = 0
        makeRequest()
        binding.tvCurrentTeam.text = classicViewModel.currentTeam
        countDownTimer.cancel()
        countDownTimer.start()
    }

    private fun navigateToScoreBreak() {
        findNavController().safeNavigate(
            ClassicFragmentDirections.actionClassicFragmentToScoreBreakFragment(
                true
            )
        )
    }


    private fun initRecycler() {
        recyclerView = binding.classicFragmentRecycler
        recyclerView.adapter = wordsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initObservers() {
        classicViewModel.currentScore.observe {
            isGameFinished = it >= gameMode.pointsToWin!!
            if (
                it > 0 && (it - (classicViewModel
                    .teams[classicViewModel.currentTeam]
                    ?: 0)) - 5 == prevCompletionPoints
            ) {
                classicViewModel.switchHasCompleted()
                prevCompletionPoints += 5
            }

            binding.tvCurrentScore.text = it.toString()
        }

        classicViewModel.hasCompleted.observe {
            if (it) {
                makeRequest()
                classicViewModel.switchHasCompleted()
            }
        }

        classicViewModel.isNextTurn.observe {
            if (it) {
                startNextTeamRound()
                classicViewModel.toggleIsNextTurn()
            }
        }
    }

    private fun initWordGetter(context: Context) {
        getWords = { classicViewModel.getFiveRandomWords(language(context)) }
    }

    private fun initGameMode() {
        gameMode = safeArgs.gameMode
        gameMode.pointsToWin = gameMode.pointsToWin ?: 90
        val map = (gameMode.teams ?: listOf("Teams1", "Teams2")).associateWith { 0 }.toMutableMap()
        classicViewModel.setTeams(map)
        timePerRound = gameMode.timePerRound ?: 30
    }


    private fun makeRequest() = viewLifecycleOwner.lifecycleScope.launch {
        wordsAdapter.setData(getWords())
    }

    private fun <T> LiveData<T>.observe(f: (T) -> Unit) = this.observe(viewLifecycleOwner) { f(it) }


}
