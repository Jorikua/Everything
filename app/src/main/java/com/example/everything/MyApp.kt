package com.example.everything

import android.app.Application
import com.example.everything.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {

  override fun onCreate() {
    super.onCreate()
    startKoin {
      if (BuildConfig.DEBUG) {
        androidLogger(Level.DEBUG)
      }
      androidContext(this@MyApp)
      modules(listOf(mainModule))
    }
  }
}