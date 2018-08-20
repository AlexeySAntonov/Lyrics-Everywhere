package com.aleksejantonov.lyricseverywhere.ui.Base

//Маркер для слияния списков
interface BaseData {

  var artistId: Int

  override fun equals(other: Any?): Boolean
}
