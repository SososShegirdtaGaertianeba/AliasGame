package com.example.alias.app

import android.app.Application
import android.widget.Spinner
import com.example.alias.R
import com.example.alias.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    companion object {
        var LANGUAGE = "English"
    }


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(viewModels))
        }

    }

}
