package com.aleksejantonov.lyricseverywhere.sl

import android.annotation.SuppressLint
import android.content.Context

object SL {
  @SuppressLint("StaticFieldLeak")
  private lateinit var componentManager: ComponentManager

  fun init(context: Context) {
    componentManager = ComponentManager(context)
  }

  fun componentManager() = componentManager
}