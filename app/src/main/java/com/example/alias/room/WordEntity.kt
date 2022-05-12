package com.example.alias.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_en")
data class WordEnglishEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "keyword")
    val name: String
)

@Entity(tableName = "words_ge")
data class WordGeorgianEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "keyword")
    val name: String
)

@Entity(tableName = "words_ru")
data class WordRussianEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "keyword")
    val name: String
)
