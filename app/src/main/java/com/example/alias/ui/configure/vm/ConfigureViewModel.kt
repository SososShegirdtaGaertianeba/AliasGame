package com.example.alias.ui.configure.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alias.util.GameMode

class ConfigureViewModel : ViewModel() {

    private val _gameMode = MutableLiveData(GameMode())
    val gameMode: LiveData<GameMode>
        get() = _gameMode

    private val _gameModeComponents =
        MutableLiveData(Array(4) { false })

    val gameModeComponenets: LiveData<Array<Boolean>>
        get() = _gameModeComponents

    private val _isTeamsInputValid = MutableLiveData(true)
    val isTeamsInputValid: LiveData<Boolean>
        get() = _isTeamsInputValid
    val handleIsTeamsInputValid: (Boolean) -> Unit = { _isTeamsInputValid.value = it }


    private fun changedGameComponents(pos: Int): Array<Boolean> {
        val res = gameModeComponenets.value
        res!![pos] = true
        return res
    }

    fun setIsClassic(isClassic: Boolean) {
        _gameMode.value?.let {
            _gameMode.value = GameMode(
                isClassic = isClassic,
                teams = it.teams,
                timePerRound = it.timePerRound,
                pointsToWin = it.pointsToWin
            )
        }

        _gameModeComponents.value = changedGameComponents(ZERO)
        Log.d("GAMEMODE", gameMode.value!!.isClassic.toString())
        Log.d("GAMEMODE", gameModeComponenets.value!!.toList().toString())
    }

    fun setTeams(teams: MutableList<String>) {
        if (teams.size != teams.toSet().size)
            handleIsTeamsInputValid(false)
        else {
            handleIsTeamsInputValid(true)
            _gameMode.value?.let {
                _gameMode.value = GameMode(
                    isClassic = it.isClassic,
                    teams = teams,
                    timePerRound = it.timePerRound,
                    pointsToWin = it.pointsToWin
                )
                _gameModeComponents.value = changedGameComponents(ONE)
            }
        }

        Log.d("GAMEMODE", gameMode.value!!.teams.toString())
        Log.d("GAMEMODE", gameModeComponenets.value!!.toList().toString())

    }

    fun setTimePerRound(timePerRound: Int?) {
        _gameMode.value?.let {
            _gameMode.value = GameMode(
                isClassic = it.isClassic,
                teams = it.teams,
                timePerRound = timePerRound,
                pointsToWin = it.pointsToWin
            )
        }
        _gameModeComponents.value = changedGameComponents(TWO)
        Log.d("GAMEMODE", gameModeComponenets.value!!.toList().toString())

    }

    fun setPointsToWin(pointsToWin: Int?) {
        _gameMode.value?.let {
            _gameMode.value = GameMode(
                isClassic = it.isClassic,
                teams = it.teams,
                timePerRound = it.timePerRound,
                pointsToWin = pointsToWin
            )
        }
        _gameModeComponents.value = changedGameComponents(THREE)
        Log.d("GAMEMODE", gameModeComponenets.value!!.toList().toString())

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