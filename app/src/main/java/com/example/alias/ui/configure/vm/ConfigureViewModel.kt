package com.example.alias.ui.configure.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alias.util.GameMode
import com.example.alias.util.PagingEvent

class ConfigureViewModel : ViewModel() {

    private val gameMode: GameMode = GameMode()

    private val isClassic =
        MutableLiveData(PagingEvent(true, 1))

    private val teams =
        MutableLiveData(PagingEvent(mutableListOf<String>(), 2))

    private val timePerRoundAndPointsToWin =
        MutableLiveData(PagingEvent(intArrayOf(30, 100), 3))

    val events = arrayOf(isClassic, teams, timePerRoundAndPointsToWin)

    fun setIsClassic(isClassic: Boolean) {
        this.isClassic.value = PagingEvent(isClassic, 1, true)
    }

    fun setTeams(teams: MutableList<String>) {
        this.teams.value = PagingEvent(teams, 2, true)
    }

    fun setTimePerRoundAndPointsToWin(timePerRound: Int, pointsToWin: Int) {
        this.timePerRoundAndPointsToWin.value =
            PagingEvent(intArrayOf(timePerRound, pointsToWin), 3, true)
    }

    fun initGameMode() {
        gameMode.isClassic = isClassic.value?.data
        gameMode.teams = teams.value?.data
        gameMode.timePerRound = timePerRoundAndPointsToWin.value?.data?.get(0)
        gameMode.pointsToWin = timePerRoundAndPointsToWin.value?.data?.get(1)
    }
}