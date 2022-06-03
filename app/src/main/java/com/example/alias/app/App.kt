package com.example.alias.app

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import com.example.alias.di.viewModels
import com.example.alias.di.wordsDao
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(viewModels, wordsDao))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            vibratorManager =
                applicationContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        else
            vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    companion object {
        lateinit var vibratorManager: VibratorManager
        lateinit var vibrator: Vibrator
    }

}
