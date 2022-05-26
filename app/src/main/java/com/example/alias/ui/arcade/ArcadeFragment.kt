package com.example.alias.ui.arcade

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alias.MainActivity.Companion.SHARED_PREFERENCE_NAME
import com.example.alias.databinding.ArcadeFragmentBinding
import com.example.alias.di.viewModels
import com.example.alias.extensions.safeNavigate
import com.example.alias.ui.arcade.vm.ArcadeViewModel
import com.example.alias.ui.base.BaseFragment
import com.example.alias.ui.home.HomeFragment.Companion.PREFERENCE_NAME
import com.example.alias.util.GameMode
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class ArcadeFragment : BaseFragment<ArcadeFragmentBinding>(ArcadeFragmentBinding::inflate) {
    private val safeArgs: ArcadeFragmentArgs by navArgs()

    private val viewModel: ArcadeViewModel by viewModel()

    private lateinit var countDownTimer: CountDownTimer

    private lateinit var getWord: suspend () -> String

    private var isGameFinished = false

    private lateinit var gameMode: GameMode


    private var timePerRound = 0

    override fun init() {
        loadKoinModules(viewModels)
        initListener()
        initGameMode()
        initWordGetter(requireContext())
        initCountDownTimer(timePerRound)
        binding.currentTeamTV.text = viewModel.currentTeam
        initObserver()
        makeRequest()
    }

    private fun initObserver() {
        viewModel.currentScore.observe(viewLifecycleOwner) {
            isGameFinished = it >= gameMode.pointsToWin!!
            binding.currentScoreTV.text = it.toString()
        }

        viewModel.isNextTurn.observe(viewLifecycleOwner) {
            if (it) {
                startNextTeamRound()
                viewModel.toggleIsNextTurn()
            }
        }
    }

    private fun startNextTeamRound() {
        viewModel.startNextTeamRound()
        makeRequest()
        binding.currentTeamTV.text = viewModel.currentTeam
        countDownTimer.cancel()
        countDownTimer.start()
    }

    private fun initWordGetter(context: Context) {
        getWord = { viewModel.getRandomWord(language(context)) }
    }

    private fun makeRequest() = viewLifecycleOwner.lifecycleScope.launch {
        binding.currentWordTV.text = getWord()
    }

    private fun initGameMode() {
        gameMode = safeArgs.gameMode
        gameMode.pointsToWin = gameMode.pointsToWin ?: 90
        val map = (gameMode.teams ?: listOf("Teams1", "Teams2")).associateWith { 0 }.toMutableMap()
        viewModel.setTeamsAndPointsToWin(map)
        timePerRound = gameMode.timePerRound ?: 30
    }

    private val language = fun(context: Context) =
        context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(
            PREFERENCE_NAME, 0
        )

    private fun initListener() {
        binding.plusBtn.setOnClickListener {
            makeRequest()
            viewModel.incrementScore()
        }

        binding.minusBtn.setOnClickListener {
            makeRequest()
            viewModel.decrementScore()
        }
    }

    private fun initCountDownTimer(timePerRound: Int) {
        countDownTimer = object : CountDownTimer(timePerRound * 1000L, 1000L) {
            override fun onTick(millisUntilFinish: Long) {
                binding.timeLeftTV.text = (millisUntilFinish / 1000).toString()
            }

            override fun onFinish() {
                viewModel.saveCurrentTeamScore()
                if (!isGameFinished)
                    navigateToScoreBreak()
                else
                    navigateToResultFragment()
            }
        }.start()
    }

    private fun navigateToResultFragment() {
        val action = ArcadeFragmentDirections.actionArcadeFragmentToResultFragment(
            gameMode,
            viewModel.teams.values.toIntArray()
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