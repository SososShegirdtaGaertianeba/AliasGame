package com.example.alias.ui.classic

import android.content.Context
import android.os.CountDownTimer
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alias.R
import com.example.alias.databinding.ClassicFragmentBinding
import com.example.alias.di.viewModels
import com.example.alias.extensions.safeNavigate
import com.example.alias.ui.base.BaseFragment
import com.example.alias.ui.classic.adapter.WordsAdapter
import com.example.alias.ui.classic.vm.ClassicViewModel
import com.example.alias.util.GameMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class ClassicFragment : BaseFragment<ClassicFragmentBinding>(ClassicFragmentBinding::inflate) {

    private lateinit var getWords: suspend () -> MutableList<String>
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var recyclerView: RecyclerView
    private lateinit var gameMode: GameMode

    // Game Logic Handler Variables
    private var isGameFinished = false
    private var gotToWinningPoints = false
    private var isBonusRound = false
    private var isStartNextTeamRequired = false

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
        initArrowBtn()
        initGameMode()
        initRecycler()
        initObservers()
        initWordGetter(requireContext())
        initCountDown(timePerRound)
        startNextTeamRound()
        onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        if (binding.tvCountDown.text == "0") {
            handleIsGameFinished()
            classicViewModel.saveCurrentTeamScore()
            handleGameContinuation()
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToEnsureDialogFragment()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
        unloadKoinModules(viewModels)
    }

    private fun initArrowBtn() {
        binding.btnShowScore.setText(getString(R.string.score))
        binding.btnShowScore.setDrawable(R.drawable.ic_arrow_up)
        binding.btnShowScore.setOnClickListener {
            if (isStartNextTeamRequired)
                classicViewModel.toggleIsNextTurn()
            else
                navigateToScoreBreak(isStartNextTeamRequired)
        }
    }

    private fun initCountDown(timePerRound: Int) {
        countDownTimer = object : CountDownTimer(timePerRound * 1000L, 1000) {
            override fun onTick(timeLeft: Long) {
                val toDisplay = timeLeft / 1000
                binding.tvCountDown.text = toDisplay.toString()
            }

            override fun onFinish() {
                isStartNextTeamRequired = true
                startNextTeamRequired()
                handleGameResumption()
            }
        }
        countDownTimer.start()
    }

    private fun startNextTeamRequired() {
        if (isStartNextTeamRequired) {
            with(binding) {
                btnShowScore.setText(getString(R.string.continue_txt))
                btnShowScore.setDrawable(R.drawable.ic_arrow_right)
                btnShowScore.setBtnColor(R.drawable.green_circle_btn_shape)
                wordsAdapter.setIsClickable(false)
            }
        } else {
            with(binding) {
                btnShowScore.setText(getString(R.string.score))
                btnShowScore.setDrawable(R.drawable.ic_arrow_up)
                btnShowScore.setBtnColor(R.drawable.circle_button_shape)
                classicFragmentRecycler.isClickable = true
                wordsAdapter.setIsClickable(true)
            }
        }
    }

    private fun handleGameResumption() {
        if (classicViewModel.hasBackPressed.value!! != 1) {
            handleIsGameFinished()
            classicViewModel.saveCurrentTeamScore()
            handleGameContinuation()
        }
    }

    private fun handleIsGameFinished() {
        isGameFinished =
            gotToWinningPoints &&
                    classicViewModel.teamPointer == classicViewModel.currentTeams.size - 1
    }

    private fun handleGameContinuation() = when {
        !isGameFinished -> navigateToScoreBreak(isStartNextTeamRequired)
        !isBonusRound -> {
            isBonusRound = true
            val leftForBonus =
                classicViewModel.currentTeams.filter { it.value >= gameMode.pointsToWin!! }

            if (leftForBonus.size <= 1)
                navigateToResultFragment()
            else {
                classicViewModel.setTeams(leftForBonus, isBonusRound)
                navigateToScoreBreak(isStartNextTeamRequired)
            }
        }
        else -> {
            val leftForBonus =
                classicViewModel.currentTeams.filter { it.value == classicViewModel.currentTeams.values.maxOrNull() }
            if (leftForBonus.size <= 1)
                navigateToResultFragment()
            else {
                classicViewModel.setTeams(leftForBonus, isBonusRound)
                navigateToScoreBreak(isStartNextTeamRequired)
            }
        }
    }


    private fun navigateToResultFragment() {
        val action = ClassicFragmentDirections.actionClassicFragmentToResultFragment(
            gameMode,
            classicViewModel.teamsTotal.values.toIntArray()
        )
        findNavController().safeNavigate(action)
    }

    private fun startNextTeamRound() {
        classicViewModel.startNextTeamRound()
        isStartNextTeamRequired = false
        startNextTeamRequired()
        prevCompletionPoints = 0
        makeRequest()
        binding.tvCurrentTeam.text = classicViewModel.currentTeam
        countDownTimer.cancel()
        countDownTimer.start()
    }

    private fun navigateToScoreBreak(isStartNextTeamRequired: Boolean) {
        findNavController().safeNavigate(
            ClassicFragmentDirections.actionClassicFragmentToScoreBreakFragment(
                true,
                isStartNextTeamRequired
            )
        )
    }


    private fun initRecycler() {
        recyclerView = binding.classicFragmentRecycler
        recyclerView.adapter = wordsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initObservers() {
        classicViewModel.hasBackPressed.observe {
            if (it >= 2) {
                lifecycleScope.launch {
                    delay(classicViewModel.dismissDuration + 40)
                    if (binding.tvCountDown.text == "0" && it == 2 || it == 3) {
                        isStartNextTeamRequired = true
                        startNextTeamRequired()
                        countDownTimer.cancel()
                        handleGameResumption()
                    }
                    classicViewModel.handleBackPress(0)
                }
            }
        }

        classicViewModel.currentScore.observe {
            if (it >= gameMode.pointsToWin!!)
                gotToWinningPoints = true

            if (
                it > 0 && (it - (classicViewModel
                    .currentTeams[classicViewModel.currentTeam]
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

    private fun navigateToEnsureDialogFragment() {
        classicViewModel.handleBackPress(1)
        findNavController().safeNavigate(
            ClassicFragmentDirections.actionClassicFragmentToEnsureInExitDialogFragment(
                true
            )
        )
    }

}
