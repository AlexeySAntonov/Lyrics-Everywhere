package com.aleksejantonov.lyricseverywhere.utils

import android.app.Activity
import android.widget.Toast

fun Activity.toast(message: String, length: Int = Toast.LENGTH_LONG) {
  Toast.makeText(this, message, length).show()
}