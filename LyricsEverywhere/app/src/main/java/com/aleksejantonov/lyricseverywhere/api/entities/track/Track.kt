package com.aleksejantonov.lyricseverywhere.api.entities.track

import android.os.Build.VERSION_CODES
import android.support.annotation.RequiresApi
import com.aleksejantonov.lyricseverywhere.ui.Base.BaseData
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Objects

data class Track(
    @SerializedName("track_id") val trackId: Int,
    @SerializedName("track_name") val trackName: String,
    @SerializedName("album_name") val albumName: String,
    @SerializedName("artist_id") override var artistId: Int,
    @SerializedName("album_coverart_100x100") val albumCover: String
) : BaseData, Serializable {

  override fun toString(): String {
    return "Track{" +
        "trackId=" + trackId +
        ", trackName='" + trackName + '\''.toString() +
        ", albumName='" + albumName + '\''.toString() +
        ", artistId=" + artistId +
        ", albumCover='" + albumCover + '\''.toString() +
        '}'.toString()
  }

  override fun equals(other: Any?): Boolean {
    if (other == null || other !is Track) return false
    return artistId == other.artistId
  }

  @RequiresApi(VERSION_CODES.KITKAT)
  override fun hashCode() = Objects.hash(trackId, trackName, albumName, artistId, albumCover)
}
