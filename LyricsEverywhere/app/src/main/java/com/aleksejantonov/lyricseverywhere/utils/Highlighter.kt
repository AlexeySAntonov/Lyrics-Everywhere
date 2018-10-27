package com.aleksejantonov.lyricseverywhere.utils

import android.content.Context
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import com.aleksejantonov.lyricseverywhere.R

fun String.highlight(query: String, context: Context): SpannableString? =
    indexOfSearchQuery(this, query)?.let {
      if (it != -1) SpannableString(this).apply { setSpan(TextAppearanceSpan(context, R.style.searchTextHighlight), it, it + query.length, 0) }
      else null
    }


private fun indexOfSearchQuery(preparingString: String, query: String?): Int? =
    query?.let { preparingString.indexOf(it, ignoreCase = true) }