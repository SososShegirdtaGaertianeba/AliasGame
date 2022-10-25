package com.example.alias.ui.configure.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alias.util.GameMode

class ConfigureViewModel : ViewModel() {

    private val _gameMode = MutableLiveData(GameMode())
    val gameMode: LiveData<GameMode>
        get() = _gameMode

    private var _viewPagerCurrentItem = MutableLiveData(0)
    val viewPagerCurrentItem: LiveData<Int>
        get() = _viewPagerCurrentItem

    private val _isTeamsInputValid = MutableLiveData(true)
    val isTeamsInputValid: LiveData<Boolean>
        get() = _isTeamsInputValid

    fun setViewPagerCurrentItem(value: Int) {
        _viewPagerCurrentItem.value = value
    }

    private fun setIsTeamsInputValid(value: Boolean) {
        _isTeamsInputValid.value = value
    }

    fun setIsClassic(isClassic: Boolean) = _gameMode.value?.let {
        _gameMode.value = GameMode(
            isClassic = isClassic,
            teams = it.teams,
            timePerRound = it.timePerRound,
            pointsToWin = it.pointsToWin
        )
    }


    fun setTeams(teams: MutableList<String>) {
        if (teams.size != teams.toSet().size)
            setIsTeamsInputValid(false)
        else {
            setIsTeamsInputValid(true)
            _gameMode.value?.let {
                _gameMode.value = GameMode(
                    isClassic = it.isClassic,
                    teams = teams,
                    timePerRound = it.timePerRound,
                    pointsToWin = it.pointsToWin
                )
            }
        }

    }

    fun setTimePerRound(timePerRound: Int?) = _gameMode.value?.let {
        _gameMode.value = GameMode(
            isClassic = it.isClassic,
            teams = it.teams,
            timePerRound = timePerRound,
            pointsToWin = it.pointsToWin
        )
    }

    fun setPointsToWin(pointsToWin: Int?) = _gameMode.value?.let {
        _gameMode.value = GameMode(
            isClassic = it.isClassic,
            teams = it.teams,
            timePerRound = it.timePerRound,
            pointsToWin = pointsToWin
        )
    }

}
