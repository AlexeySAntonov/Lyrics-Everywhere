package com.aleksejantonov.lyricseverywhere.ui.base

//Маркер для слияния списков
interface BaseData : ListItem {

  var artistId: Long

  override fun equals(other: Any?): Boolean
}
