package com.example.alias.ui.configure.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alias.util.GameMode
import com.example.alias.util.PagingEvent
import kotlinx.coroutines.flow.MutableStateFlow

class ConfigureViewModel : ViewModel() {

    val gameMode: GameMode = GameMode()

    private val isClassic =
        MutableLiveData(PagingEvent(true, ONE))

    val hasGameModeBeenInitialized =
        MutableLiveData(PagingEvent(false, ZERO))

    private val teams =
        MutableLiveData(PagingEvent(mutableListOf<String>(), TWO))

    private val timePerRoundAndPointsToWin =
        MutableLiveData(PagingEvent(intArrayOf(DEFAULT_SCORE, DEFAULT_TIME), THREE))

    val events = arrayOf(isClassic, teams, timePerRoundAndPointsToWin)

    fun setIsClassic(isClassic: Boolean) {
        this.isClassic.value = PagingEvent(isClassic, ONE, true)
    }

    fun setTeams(teams: MutableList<String>) {
        this.teams.value = PagingEvent(teams, TWO, true)
    }

    fun setTimePerRoundAndPointsToWin(timePerRound: Int, pointsToWin: Int) {
        this.timePerRoundAndPointsToWin.value =
            PagingEvent(intArrayOf(timePerRound, pointsToWin), THREE, true)
    }

    fun initGameMode() {
        gameMode.isClassic = isClassic.value?.data ?: true
        gameMode.teams = teams.value?.data ?: listOf("Team1", "Team2")
        gameMode.timePerRound = timePerRoundAndPointsToWin.value?.data?.get(ZERO) ?: 60
        gameMode.pointsToWin = timePerRoundAndPointsToWin.value?.data?.get(ONE) ?: 100
        hasGameModeBeenInitialized.value = PagingEvent(true, ZERO, true)
    }

    companion object {
        private const val ZERO = 0
        private const val ONE = 1
        private const val TWO = 2
        private const val THREE = 3
        private const val DEFAULT_SCORE = 30
        private const val DEFAULT_TIME = 100
    }
}