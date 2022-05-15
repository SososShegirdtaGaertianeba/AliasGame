package com.example.alias.di

import com.example.alias.room.WordsDatabase
import com.example.alias.ui.arcade.vm.ArcadeViewModel
import com.example.alias.ui.classic.vm.ClassicViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    single { ArcadeViewModel(get()) }
    single { ClassicViewModel(get()) }
}

val wordsDao = module {
    single {
        WordsDatabase
            .getInstance(
                androidApplication()
                    .applicationContext
            )
            .wordsDao
    }
}
