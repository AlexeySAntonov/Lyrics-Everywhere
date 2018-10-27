package com.aleksejantonov.lyricseverywhere.utils

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

fun Activity.toast(message: String, length: Int = Toast.LENGTH_LONG) {
  Toast.makeText(this, message, length).show()
}

fun View.showKeyboard() = postDelayed(
    { (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, 0) },
    500L
)

fun View.hideKeyboard() = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    .hideSoftInputFromWindow(windowToken, 0)

fun Fragment.getColor(@ColorRes colorId: Int): Int = context?.let { ContextCompat.getColor(it, colorId) } ?: 0

fun SearchView.setTextColor(color: Int) =
    this.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text)
        .setTextColor(color)

fun SearchView.setUnderscoreColor(color: Int) =
    this.findViewById<View>(android.support.v7.appcompat.R.id.search_plate)
        .background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)

fun SearchView.textChangeListener(action: (String) -> Unit) =
    this.setOnQueryTextListener(object : OnQueryTextListener {
      override fun onQueryTextSubmit(p0: String) = false
      override fun onQueryTextChange(p0: String): Boolean {
        action.invoke(p0)
        return false
      }
    })