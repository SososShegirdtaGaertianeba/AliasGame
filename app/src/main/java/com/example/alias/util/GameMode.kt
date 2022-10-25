package com.example.alias.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameMode(
    var isClassic: Boolean? = null,
    var teams: List<String>? = null,
    var timePerRound: Int? = null,
    var pointsToWin: Int? = null
) : Parcelable
