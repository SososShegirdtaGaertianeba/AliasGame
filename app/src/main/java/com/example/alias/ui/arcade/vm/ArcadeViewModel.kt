package com.example.alias.ui.arcade.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alias.room.WordsDao
import kotlinx.coroutines.withContext

class ArcadeViewModel(private val wordsDao: WordsDao) : ViewModel() {

    private val wordSet = mutableSetOf<String?>()
    private var _teams = mutableMapOf<String, Int>()
    private val _currentScore = MutableLiveData(0)
    private var _currentTeam = "Team1"

    private var teamPointer = 0

    private val teamsList: List<String>
        get() = _teams.keys.toList()

    val currentScore: LiveData<Int>
        get() = _currentScore

    val currentTeam: String
        get() = _currentTeam

    private val _isNextTurn =
        MutableLiveData(false)

    val isNextTurn: LiveData<Boolean>
        get() = _isNextTurn

    val teams: MutableMap<String, Int>
        get() = _teams

    private var currentWord = ""

    fun toggleIsNextTurn() {
        _isNextTurn.value = !_isNextTurn.value!!
    }

    fun startNextTeamRound() {
        teamPointer = (teamPointer + 1) % teamsList.size
        _currentTeam = teamsList[teamPointer]
        getCurrentTeamScore()
    }

    fun incrementScore() {
        _currentScore.value = _currentScore.value!! + 1
    }

    fun decrementScore() {
        _currentScore.value = _currentScore.value!! - 1
    }

    fun saveCurrentTeamScore() {
        val score = _teams[currentTeam]
        score?.let {
            _teams[currentTeam] =
                it + (currentScore.value!! - it)
        }
    }

    fun setTeamsAndPointsToWin(teams: MutableMap<String, Int>) {
        this._teams = teams
        _currentTeam = teamsList[teamPointer]
    }

    private fun getCurrentTeamScore() {
        _currentScore.value = _teams[currentTeam] ?: 0
    }

    private suspend fun getRandomEnglishWord() {
        currentWord = wordsDao.getRandomEnglishWord().name
    }

    private suspend fun getRandomRussianWord() {
        currentWord = wordsDao.getRandomRussianWord().name
    }

    private suspend fun getRandomGeorgianWord() {
        currentWord = wordsDao.getRandomGeorgianWord().name
    }

    private suspend fun getRandomWord(getter: suspend () -> Unit): String =
        withContext(viewModelScope.coroutineContext) {
            while (!wordSet.add(currentWord))
                getter()
            currentWord
        }

    suspend fun getRandomWord(language: Int) = when (language) {
        1 -> getRandomWord { getRandomRussianWord() }
        2 -> getRandomWord { getRandomGeorgianWord() }
        else -> getRandomWord { getRandomEnglishWord() }
    }
}