package com.example.alias.ui.classic.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alias.room.WordsDao
import kotlinx.coroutines.withContext

class ClassicViewModel(
    private val wordsDao: WordsDao
) : ViewModel() {

    private val wordSet = mutableSetOf<String?>()
    private val _teams = MutableLiveData<MutableMap<String, Int>>()
    private val _currentScore = MutableLiveData(0)
    private val _hasCompleted = MutableLiveData(false)
    private val _currentTeam = MutableLiveData("Team1")


    private var teamPointer = 0

    private val teamsList: List<String>
        get() = _teams.value!!.keys.toList()

    val hasCompleted: LiveData<Boolean>
        get() = _hasCompleted

    val currentScore: LiveData<Int>
        get() = _currentScore

    val currentTeam: LiveData<String>
        get() = _currentTeam

    val teams: LiveData<MutableMap<String, Int>>
        get() = _teams

    private var _pointsToWin = -1


    private var currentWord = ""


    fun startNextTeamRound() {
        saveCurrentTeamScore()
        teamPointer = (teamPointer + 1) % teamsList.size
        _currentTeam.value = teamsList[teamPointer]
        getCurrentTeamScore()
    }

    fun incrementScore() {
        _currentScore.value = _currentScore.value!! + 1
    }

    fun decrementScore() {
        _currentScore.value = _currentScore.value!! - 1
    }

    private fun saveCurrentTeamScore() {
        val score = _teams.value?.get(currentTeam.value)
        score?.let {
            _teams.value!![currentTeam.value!!] =
                it + (currentScore.value!! - it)
        }
    }

    private fun getCurrentTeamScore() {
        _currentScore.value = _teams.value?.get(currentTeam.value) ?: 0
    }

    fun switchHasCompleted() {
        _hasCompleted.value = !_hasCompleted.value!!
    }

    fun setTeamsAndPointsToWin(teams: MutableMap<String, Int>, pointsToWin: Int) {
        this._teams.value = teams
        this._pointsToWin = pointsToWin
        _currentTeam.value = teamsList[teamPointer]
        Log.d("TEAMS_AND_POINTS", teams.toString())
    }

    private suspend fun getRandomEnglishWord() {
        currentWord =
            wordsDao.getRandomEnglishWord().name
    }

    private suspend fun getRandomGeorgianWord() {
        currentWord =
            wordsDao.getRandomGeorgianWord().name
    }

    private suspend fun getRandomRussianWord() {
        currentWord =
            wordsDao.getRandomRussianWord().name
    }

    private suspend fun getFiveRandomWords(getter: suspend () -> Unit): MutableList<String> =
        withContext(viewModelScope.coroutineContext) {
            val res = mutableListOf<String>()
            while (res.size < 5) {
                getter()
                currentWord.let {
                    if (wordSet.add(it)) {
                        res.add(it)
                    }
                }
            }
            res
        }

    suspend fun getFiveRandomWords(language: Int) = when (language) {
        1 -> getFiveRandomWords { getRandomRussianWord() }
        2 -> getFiveRandomWords { getRandomGeorgianWord() }
        else -> getFiveRandomWords { getRandomEnglishWord() }
    }

}
