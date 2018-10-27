package com.aleksejantonov.lyricseverywhere.utils

import android.content.Context
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import com.aleksejantonov.lyricseverywhere.R

fun String.highlight(query: String, context: Context): SpannableString? {
  indexOfSearchQuery(this, query).let {
    if (it == -1) return null
    return SpannableString(this).apply {
      setSpan(TextAppearanceSpan(context, R.style.searchTextHighlight), it, it + query.length, 0)
    }
  }
}

private fun indexOfSearchQuery(preparingString: String, query: String): Int {
  return if (!TextUtils.isEmpty(query)) {
    preparingString.toLowerCase().indexOf(query.toLowerCase())
  } else -1
}