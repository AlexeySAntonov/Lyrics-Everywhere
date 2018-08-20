package com.aleksejantonov.lyricseverywhere.api.entities.artist

import com.aleksejantonov.lyricseverywhere.ui.Base.BaseData
import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("artist_id") override var artistId: Int,
    @SerializedName("artist_name") val artistName: String,
    @SerializedName("artist_twitter_url") val twitterUrl: String
) : BaseData {

  var topChartCountries = ""

  override fun toString(): String {
    return "Artist{" +
        "artistName='" + artistName + '\''.toString() +
        ", artistId=" + artistId +
        '}'.toString()
  }

  override fun equals(o: Any?): Boolean {
    if (o !is Artist) {
      return false
    }
    val artist = o as Artist?
    return artistId == artist!!.artistId
  }
}
