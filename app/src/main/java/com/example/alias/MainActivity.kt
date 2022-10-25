package com.example.alias

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.alias.ui.home.HomeFragment.Companion.PREFERENCE_DEFAULT_VALUE
import com.example.alias.ui.home.HomeFragment.Companion.PREFERENCE_NAME
import com.example.alias.util.getLanguageFromId
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            ContextWrapper(
                newBase.setAppLocale(
                    getLanguageFromId(
                        newBase.getSharedPreferences(
                            SHARED_PREFERENCE_NAME,
                            Context.MODE_PRIVATE
                        )
                            .getInt(PREFERENCE_NAME, PREFERENCE_DEFAULT_VALUE)
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

    companion object {
        const val SHARED_PREFERENCE_NAME = "languageSharedPreference"
    }
}
