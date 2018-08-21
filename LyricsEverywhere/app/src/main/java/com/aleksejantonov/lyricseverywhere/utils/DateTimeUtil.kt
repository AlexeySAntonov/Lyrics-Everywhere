package com.aleksejantonov.lyricseverywhere.utils

import java.util.Calendar

object DateTimeUtil {

  fun isNightModeNecessary(): Boolean {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    if (hour in 0..6 || hour in 20..23) return true
    return false
  }
}