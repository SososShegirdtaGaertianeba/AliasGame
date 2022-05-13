package com.example.alias.ui.classic.vm

import androidx.lifecycle.ViewModel
import com.example.alias.room.WordsDao

class ClassicViewModel(
    private val wordsDao: WordsDao
) : ViewModel() {

    fun getRandomWord() {

    }
}