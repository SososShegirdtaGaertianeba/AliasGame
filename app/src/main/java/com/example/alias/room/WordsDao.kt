package com.example.alias.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WordsDao {

    @Query("SELECT * FROM words_en ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomEnglishWord(): WordEnglishEntity

    @Query("SELECT * FROM words_ge ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomGeorgianWord(): WordGeorgianEntity

    @Query("SELECT * FROM words_ru ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomRussianWord(): WordRussianEntity
}
