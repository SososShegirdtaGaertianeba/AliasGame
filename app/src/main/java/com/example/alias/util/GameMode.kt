package com.example.alias.util

data class GameMode(
    var isClassic: Boolean? = null,
    var teams: List<String>? = null,
    var timePerRound: Int? = null,
    var pointsToWin: Int? = null
)