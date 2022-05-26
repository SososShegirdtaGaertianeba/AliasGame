package com.example.alias.ui.arcade

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alias.MainActivity.Companion.SHARED_PREFERENCE_NAME
import com.example.alias.databinding.ArcadeFragmentBinding
import com.example.alias.di.viewModels
import com.example.alias.safeNavigate
import com.example.alias.ui.arcade.vm.ArcadeViewModel
import com.example.alias.ui.base.BaseFragment
import com.example.alias.ui.home.HomeFragment.Companion.PREFERENCE_NAME
import com.example.alias.util.GameMode
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class ArcadeFragment : BaseFragment<ArcadeFragmentBinding>(ArcadeFragmentBinding::inflate) {

    private lateinit var getWord: suspend () -> String
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var gameMode: GameMode

    // Game Logic Handler Variables
    private var isGameFinished = false
    private var gotToWinningPoints = false
    private var isBonusRound = false
    private var pointer = 0

    private val safeArgs: ArcadeFragmentArgs by navArgs()
    private val arcadeViewModel: ArcadeViewModel by viewModel()


    private var timePerRound = 0

    override fun init() {
        loadKoinModules(viewModels)
        initListener()
        initGameMode()
        initObservers()
        initWordGetter(requireContext())
        initCountDownTimer(timePerRound)
        startNextTeamRound()
    }

    private fun initObservers() {
        arcadeViewModel.currentScore.observe(viewLifecycleOwner) {
            if (it >= gameMode.pointsToWin!!)
                gotToWinningPoints = true

            binding.currentScoreTV.text = it.toString()
        }

        arcadeViewModel.isNextTurn.observe(viewLifecycleOwner) {
            if (it) {
                startNextTeamRound()
                arcadeViewModel.toggleIsNextTurn()
            }
        }
    }

    private fun handleIsGameFinished() {
        isGameFinished =
            gotToWinningPoints &&
                    pointer == arcadeViewModel.currentTeams.size - 1
        pointer = (pointer + 1) % arcadeViewModel.currentTeams.size
    }

    private fun handleGameContinuation() = when {
        !isGameFinished -> navigateToScoreBreak()
        !isBonusRound -> {
            isBonusRound = true
            val leftForBonus =
                arcadeViewModel.currentTeams.filter { it.value >= gameMode.pointsToWin!! }

            if (leftForBonus.size <= 1)
                navigateToResultFragment()
            else {
                arcadeViewModel.setTeams(leftForBonus, isBonusRound)
                navigateToScoreBreak()
            }
        }
        else -> {
            val leftForBonus =
                arcadeViewModel.currentTeams.filter { it.value == arcadeViewModel.currentTeams.values.maxOrNull() }

            if (leftForBonus.size <= 1)
                navigateToResultFragment()
            else {
                arcadeViewModel.setTeams(leftForBonus, isBonusRound)
                navigateToScoreBreak()
            }
        }
    }

    private fun startNextTeamRound() {
        arcadeViewModel.startNextTeamRound()
        makeRequest()
        binding.currentTeamTV.text = arcadeViewModel.currentTeam
        countDownTimer.cancel()
        countDownTimer.start()
    }

    private fun initWordGetter(context: Context) {
        getWord = { arcadeViewModel.getRandomWord(language(context)) }
    }

    private fun makeRequest() = viewLifecycleOwner.lifecycleScope.launch {
        binding.currentWordTV.text = getWord()
    }

    private fun initGameMode() {
        gameMode = safeArgs.gameMode
        gameMode.pointsToWin = gameMode.pointsToWin ?: 90
        val map = (gameMode.teams ?: listOf("Teams1", "Teams2")).associateWith { 0 }.toMutableMap()
        arcadeViewModel.setTeams(map)
        timePerRound = gameMode.timePerRound ?: 30
    }

    private val language = fun(context: Context) =
        context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(
            PREFERENCE_NAME, 0
        )

    private fun initListener() {
        binding.plusBtn.setOnClickListener {
            makeRequest()
            arcadeViewModel.incrementScore()
        }

        binding.minusBtn.setOnClickListener {
            makeRequest()
            arcadeViewModel.decrementScore()
        }
    }

    private fun initCountDownTimer(timePerRound: Int) {
        countDownTimer = object : CountDownTimer(timePerRound * 1000L, 1000L) {
            override fun onTick(millisUntilFinish: Long) {
                binding.timeLeftTV.text = (millisUntilFinish / 1000).toString()
            }

            override fun onFinish() {
                handleIsGameFinished()
                arcadeViewModel.saveCurrentTeamScore()
                handleGameContinuation()
            }
        }.start()
    }

    private fun navigateToResultFragment() {
        val action = ArcadeFragmentDirections.actionArcadeFragmentToResultFragment(
            gameMode,
            arcadeViewModel.teamsTotal.values.toIntArray()
        )
        findNavController().navigate(action)
    }

    private fun navigateToScoreBreak() {
        findNavController().safeNavigate(
            ArcadeFragmentDirections.actionArcadeFragmentToScoreBreakFragment(
                false
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
        unloadKoinModules(viewModels)
    }
}