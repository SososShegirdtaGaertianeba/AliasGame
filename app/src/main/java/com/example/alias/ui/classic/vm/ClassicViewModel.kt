package com.example.alias.ui.classic.vm

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
    private var _teamsTotal = mutableMapOf<String, Int>()
    private var _currentTeams = mutableMapOf<String, Int>()
    private val _currentScore = MutableLiveData(0)
    private val _hasCompleted = MutableLiveData(false)
    private var _currentTeam = "Team1"

    val currentTeams: Map<String, Int>
        get() = _currentTeams

    val teamsTotal: Map<String, Int>
        get() = _teamsTotal

    private var teamPointer = 0

    private val _isNextTurn =
        MutableLiveData(false)

    private val teamsList: List<String>
        get() = this._currentTeams.keys.toList()

    val isNextTurn: LiveData<Boolean>
        get() = _isNextTurn

    val hasCompleted: LiveData<Boolean>
        get() = _hasCompleted

    val currentScore: LiveData<Int>
        get() = _currentScore

    val currentTeam: String
        get() = _currentTeam

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
        val score = this._currentTeams[currentTeam]
        score?.let {
            this._currentTeams[currentTeam] =
                it + (currentScore.value!! - it)
            _teamsTotal[currentTeam] = currentTeams[currentTeam]!!
        }
    }

    private fun getCurrentTeamScore() {
        _currentScore.value = this._currentTeams[currentTeam] ?: 0
    }

    fun switchHasCompleted() {
        _hasCompleted.value = !_hasCompleted.value!!
    }

    fun setTeams(teams: Map<String, Int>, isBonusRound: Boolean = false) {
        if (!isBonusRound)
            this._teamsTotal = teams.toMutableMap()
        teamPointer = -1
        this._currentTeams = teams.toMutableMap()
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

    private suspend fun getFiveRandomWords(getWord: suspend () -> Unit): MutableList<String> =
        withContext(viewModelScope.coroutineContext) {
            val res = mutableListOf<String>()
            while (res.size < 5) {
                getWord()
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
