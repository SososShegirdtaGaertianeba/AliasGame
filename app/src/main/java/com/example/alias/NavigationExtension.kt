package com.example.alias

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigate(direction: NavDirections) {
    Log.d("clickTag", "Click happened")
    currentDestination?.getAction(direction.actionId)?.run {
        Log.d("clickTag", "Click Propagated")
        navigate(direction)
    }
}