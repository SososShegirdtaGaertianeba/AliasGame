package com.example.alias.di

import com.example.alias.ui.arcade.vm.ArcadeViewModel
import com.example.alias.ui.classic.vm.ClassicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    viewModel { ArcadeViewModel(get()) }
    viewModel { ClassicViewModel(get()) }
}