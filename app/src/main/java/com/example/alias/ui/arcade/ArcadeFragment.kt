package com.example.alias.ui.arcade

import android.content.Context
import android.os.CountDownTimer
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alias.MainActivity.Companion.SHARED_PREFERENCE_NAME
import com.example.alias.R
import com.example.alias.databinding.ArcadeFragmentBinding
import com.example.alias.di.viewModels
import com.example.alias.extensions.safeNavigate
import com.example.alias.ui.arcade.vm.ArcadeViewModel
import com.example.alias.ui.base.BaseFragment
import com.example.alias.ui.home.HomeFragment.Companion.PREFERENCE_NAME
import com.example.alias.util.GameMode
import kotlinx.coroutines.delay
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
    private var isStartNextTeamRequired = false

    private val safeArgs: ArcadeFragmentArgs by navArgs()
    private val arcadeViewModel: ArcadeViewModel by viewModel()


    private var timePerRound = 0

    override fun init() {
        loadKoinModules(viewModels)
        initArrowBtn()
        initListener()
        initGameMode()
        initWordGetter(requireContext())
        makeRequest()
        initObservers()
        initCountDownTimer(timePerRound)
        startNextTeamRound()
        onBackPressed()
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToEnsureDialogFragment()
            }
        })
    }

    private fun initArrowBtn() {
        binding.btnShowScore.setText(getString(R.string.score))
        binding.btnShowScore.setDrawable(R.drawable.ic_arrow_up)
        binding.btnShowScore.setOnClickListener {
            if (isStartNextTeamRequired)
                arcadeViewModel.toggleIsNextTurn()
            else
                navigateToScoreBreak(isStartNextTeamRequired)
        }
    }

    private fun initObservers() {
        arcadeViewModel.currentScore.observe {
            if (it >= gameMode.pointsToWin!!)
                gotToWinningPoints = true

            binding.currentScoreTV.text = it.toString()
        }

        arcadeViewModel.isNextTurn.observe {
            if (it) {
                startNextTeamRound()
                arcadeViewModel.toggleIsNextTurn()
            }
        }

        arcadeViewModel.hasBackPressed.observe {
            if (it >= 2) {
                lifecycleScope.launch {
                    delay(arcadeViewModel.dismissDuration + 40)
                    if (binding.timeLeftTV.text == ZERO && it == 2 || it == 3) {
                        isStartNextTeamRequired = true
                        startNextTeamRequired()
                        countDownTimer.cancel()
                        handleGameResumption()
                    }
                    arcadeViewModel.handleBackPress(0)

                }
            }
        }
    }

    private fun startNextTeamRequired() {
        if (isStartNextTeamRequired) {
            with(binding) {
                btnShowScore.setText(getString(R.string.continue_txt))
                btnShowScore.setDrawable(R.drawable.ic_arrow_right)
                btnShowScore.setBtnColor(R.drawable.green_circle_btn_shape)
                plusBtn.isClickable = false
                minusBtn.isClickable = false
            }

        } else {
            with(binding) {
                btnShowScore.setText(getString(R.string.score))
                btnShowScore.setDrawable(R.drawable.ic_arrow_up)
                btnShowScore.setBtnColor(R.drawable.circle_button_shape)
                plusBtn.isClickable = true
                minusBtn.isClickable = true
            }
        }
    }

    private fun handleIsGameFinished() {
        isGameFinished =
            gotToWinningPoints &&
                    arcadeViewModel.teamPointer == arcadeViewModel.currentTeams.size - 1
    }

    private fun handleGameContinuation() {
        when {
            !isGameFinished -> {
                navigateToScoreBreak(isStartNextTeamRequired)
            }
            !isBonusRound -> {
                isBonusRound = true
                val leftForBonus =
                    arcadeViewModel.currentTeams.filter { it.value >= gameMode.pointsToWin!! }

                if (leftForBonus.size <= 1)
                    navigateToResultFragment()
                else {
                    arcadeViewModel.setTeams(leftForBonus, isBonusRound)
                    navigateToScoreBreak(isStartNextTeamRequired)
                }
            }
            else -> {
                val leftForBonus =
                    arcadeViewModel.currentTeams.filter { it.value == arcadeViewModel.currentTeams.values.maxOrNull() }

                if (leftForBonus.size <= 1)
                    navigateToResultFragment()
                else {
                    arcadeViewModel.setTeams(leftForBonus, isBonusRound)
                    navigateToScoreBreak(isStartNextTeamRequired)
                }
            }
        }
    }

    private fun startNextTeamRound() {
        arcadeViewModel.startNextTeamRound()
        isStartNextTeamRequired = false
        startNextTeamRequired()
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
        val map = (gameMode.teams ?: listOf(TEAMS_1, TEAMS_2)).associateWith { 0 }.toMutableMap()
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
                isStartNextTeamRequired = true
                startNextTeamRequired()
                handleGameResumption()
            }
        }.start()
    }

    private fun handleGameResumption() {
        if (arcadeViewModel.hasBackPressed.value!! != 1) {
            handleIsGameFinished()
            arcadeViewModel.saveCurrentTeamScore()
            handleGameContinuation()
        }
    }

    private fun navigateToResultFragment() {
        val action = ArcadeFragmentDirections.actionArcadeFragmentToResultFragment(
            gameMode,
            arcadeViewModel.teamsTotal.values.toIntArray()
        )
        findNavController().safeNavigate(action)
    }

    private fun navigateToScoreBreak(isStartNextTeamRoundRequired: Boolean) {
        findNavController().safeNavigate(
            ArcadeFragmentDirections.actionArcadeFragmentToScoreBreakFragment(
                false,
                isStartNextTeamRoundRequired
            )
        )
    }

    private fun navigateToEnsureDialogFragment() {
        arcadeViewModel.handleBackPress(1)
        findNavController().safeNavigate(
            ArcadeFragmentDirections.actionArcadeFragmentToEnsureInExitDialogFragment(false)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
        unloadKoinModules(viewModels)
    }

    private fun <T> LiveData<T>.observe(f: (T) -> Unit) = this.observe(viewLifecycleOwner) { f(it) }

    companion object {
        private const val ZERO = "0"
        private const val TEAMS_1 = "Teams 1"
        private const val TEAMS_2 = "Teams 2"

    }
}