package com.aleksejantonov.lyricseverywhere.api.entities.artist

import android.os.Build.VERSION_CODES
import android.support.annotation.RequiresApi
import com.aleksejantonov.lyricseverywhere.ui.base.BaseData
import com.google.gson.annotations.SerializedName
import java.util.Objects

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

  override fun equals(other: Any?): Boolean {
    if (other == null || other !is Artist) return false
    return artistId == other.artistId
  }

  @RequiresApi(VERSION_CODES.KITKAT)
  override fun hashCode() = Objects.hash(artistId, artistName, twitterUrl)
}
