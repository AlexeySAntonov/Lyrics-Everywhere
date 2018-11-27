package com.aleksejantonov.lyricseverywhere

import android.app.Application
import com.aleksejantonov.lyricseverywhere.sl.SL
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class MyApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    Stetho.initializeWithDefaults(this)

    initLeakCanary()
    initTimber()
    SL.init(this)
  }

  private fun initLeakCanary() {
    if (BuildConfig.DEBUG) {
      if (LeakCanary.isInAnalyzerProcess(this)) return
      LeakCanary.install(this)
    }
  }

  private fun initTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}
