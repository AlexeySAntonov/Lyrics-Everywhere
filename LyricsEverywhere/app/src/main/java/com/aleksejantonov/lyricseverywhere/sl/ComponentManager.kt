package com.aleksejantonov.lyricseverywhere.sl

import android.content.Context

class ComponentManager(private val context: Context) {
  val appComponent by lazy { AppComponent(context) }
}