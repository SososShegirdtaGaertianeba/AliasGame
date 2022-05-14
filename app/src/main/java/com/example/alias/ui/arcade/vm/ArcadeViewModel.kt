package com.example.alias.ui.arcade.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alias.room.WordsDao
import kotlinx.coroutines.launch

class ArcadeViewModel(private val wordsDao: WordsDao) : ViewModel() {

    private val _currentWord = MutableLiveData<String>()
    val currentWord : LiveData<String>
        get() = _currentWord

    fun getRandomEnglishWord() {
        viewModelScope.launch {
            _currentWord.value = wordsDao.getRandomEnglishWord().name
        }
    }

    fun getRandomRussianWord() {
        viewModelScope.launch {
            _currentWord.value = wordsDao.getRandomRussianWord().name
        }
    }

    fun getRandomGeorgianWord() {
        viewModelScope.launch {
            _currentWord.value = wordsDao.getRandomGeorgianWord().name
        }
    }

}