package com.aleksejantonov.lyricseverywhere.di

import android.annotation.SuppressLint
import android.content.Context

object DI {
  @SuppressLint("StaticFieldLeak")
  private lateinit var componentManager: ComponentManager

  fun init(context: Context) {
    componentManager = ComponentManager(context)
  }

  fun componentManager() = componentManager
}