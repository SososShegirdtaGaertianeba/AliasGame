package com.example.alias

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.alias.app.App
import com.example.alias.util.LocaleUtils
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            ContextWrapper(
                newBase.setAppLocale(
                    LocaleUtils.getLanguageFromId(
                        App.LANGUAGE
                    )
                )
            )
        )
    }

    private fun Context.setAppLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }
}
