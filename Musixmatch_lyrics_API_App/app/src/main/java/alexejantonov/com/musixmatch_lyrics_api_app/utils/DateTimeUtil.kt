package alexejantonov.com.musixmatch_lyrics_api_app.utils

import java.util.Calendar

object DateTimeUtil {

  fun isNightModeNecessary(): Boolean {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    if (hour in 0..6 && hour in 18..23) return true
    return false
  }
}