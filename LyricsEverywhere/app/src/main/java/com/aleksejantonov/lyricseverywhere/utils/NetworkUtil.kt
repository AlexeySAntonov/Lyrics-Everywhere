package com.aleksejantonov.lyricseverywhere.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {
  fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = connectivityManager.activeNetworkInfo
    return info != null && info.isConnectedOrConnecting
  }
}