package com.example.alias.util

object LocaleUtils {
    fun getLanguageFromId(id: Int) = when(id) {
        0 -> "en"
        1 -> "ru"
        2 -> "ka"
        else -> "en"
    }

    fun getIdFromLanguage(language: String) = when(language) {
        "en" -> 0
        "ru" -> 1
        "ka" -> 2
        else -> 0
    }
}