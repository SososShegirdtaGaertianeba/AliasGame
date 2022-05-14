package com.example.alias.ui.classic.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alias.room.WordsDao
import com.example.alias.util.GameMode
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class ClassicViewModel(
    private val wordsDao: WordsDao
) : ViewModel() {
    private val wordSet = mutableSetOf<String>()

    val teams = MutableLiveData<Map<String, Int>>()
    private var pointsToWin = -1


    val currentWord = MutableLiveData<String>("")

    fun getRandomEnglishWord() = viewModelScope.launch {
        currentWord.value =
            wordsDao.getRandomEnglishWord().name
    }

    fun setTeamsAndPointsToWin(teams: Map<String, Int>, pointsToWin: Int) {
        this.teams.value = teams
        this.pointsToWin = pointsToWin
    }

    private fun getRandomGeorgianWord() = viewModelScope.launch {
        currentWord.value =
            wordsDao.getRandomGeorgianWord().name
    }

    private fun getRandomRussianWord() = viewModelScope.launch {
        currentWord.value =
            wordsDao.getRandomRussianWord().name
    }

//    fun getFiveRandomEnglishWords(): MutableList<String> {
//        var count = 0
//        val result = mutableListOf<String>()
//        while (count < 5) {
//            getRandomEnglishWord()
//            if (wordSet.add(currentWord.value)) {
//                result.add(currentWord.value)
//                count++
//            }
//        }
//        return result
//    }
//
//    fun getFiveRandomRussianWords(): MutableList<String> {
//        var count = 0
//        val result = mutableListOf<String>()
//        while (count < 5) {
//            getRandomRussianWord()
//            if (wordSet.add(currentWord.value)) {
//                result.add(currentWord.value)
//                count++
//            }
//        }
//        return result
//    }
//
//    fun getFiveRandomGeorgianWords(): MutableList<String> {
//        var count = 0
//        val result = mutableListOf<String>()
//        while (count < 5) {
//            getRandomGeorgianWord()
//            if (wordSet.add(currentWord.value)) {
//                result.add(currentWord.value)
//                count++
//            }
//        }
//        return result
//    }
}