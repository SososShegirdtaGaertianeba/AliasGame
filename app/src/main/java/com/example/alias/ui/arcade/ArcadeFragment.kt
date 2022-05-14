package com.example.alias.ui.arcade

import android.content.Context
import android.os.CountDownTimer
import com.example.alias.MainActivity.Companion.SHARED_PREFERENCE_NAME
import com.example.alias.databinding.ArcadeFragmentBinding
import com.example.alias.ui.arcade.vm.ArcadeViewModel
import com.example.alias.ui.base.BaseFragment
import com.example.alias.ui.home.HomeFragment.Companion.PREFERENCE_NAME
import com.example.alias.util.GameMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArcadeFragment : BaseFragment<ArcadeFragmentBinding>(ArcadeFragmentBinding::inflate) {
//    private lateinit var safeArgs:

    private val viewModel: ArcadeViewModel by viewModel()

    private lateinit var countDownTimer: CountDownTimer

    private lateinit var getWord: () -> Unit

    private val gameMode = GameMode(false, listOf("Team1", "Team2"), 10, 60)

    override fun init() {
        initListener()
        initObserver()
        configureRequest(language(requireContext()))
        getWord()
        initCountDownTimer()
    }

    private val language = fun(context: Context) =
        context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(
            PREFERENCE_NAME, 0
        )

    private fun configureRequest(language: Int) {
        getWord = when (language) {
            0 -> {
                { viewModel.getRandomEnglishWord() }
            }
            1 -> {
                { viewModel.getRandomRussianWord() }
            }
            2 -> {
                { viewModel.getRandomGeorgianWord() }
            }
            else -> {
                { viewModel.getRandomEnglishWord() }
            }
        }
    }

    private fun initListener() {
        binding.plusBtn.setOnClickListener {
            getWord()
        }

        binding.minusBtn.setOnClickListener {
            getWord()
        }

    }

    private fun initObserver() {
        viewModel.currentWord.observe(viewLifecycleOwner) {
            binding.textView.text = it
        }
    }

    private fun initCountDownTimer() {
        countDownTimer = object : CountDownTimer(gameMode.timePerRound!! * 1000L, 1000L) {
            override fun onTick(millisUntilFinish: Long) {
                binding.currentTeamTV.text = (millisUntilFinish / 1000).toString()
            }

            override fun onFinish() {
                makeToastMessage("time ended")
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }
}