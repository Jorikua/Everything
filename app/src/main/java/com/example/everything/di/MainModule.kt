package com.example.everything.di

import com.example.everything.MainViewModel
import com.example.everything.Repository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
  viewModel {
    MainViewModel(get())
  }

  single {
    Repository()
  }
}