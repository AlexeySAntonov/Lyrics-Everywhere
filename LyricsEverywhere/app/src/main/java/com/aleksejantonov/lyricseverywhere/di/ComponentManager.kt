package com.aleksejantonov.lyricseverywhere.di

import android.content.Context

class ComponentManager(private val context: Context) {
  val appComponent by lazy { AppComponent(context) }
}