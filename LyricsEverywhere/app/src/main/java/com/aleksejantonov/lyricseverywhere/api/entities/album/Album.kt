package com.aleksejantonov.lyricseverywhere.api.entities.album

import android.os.Build.VERSION_CODES
import android.support.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.util.Objects

data class Album(
    @SerializedName("album_id") val albumId: Long,
    @SerializedName("album_name") val albumName: String,
    @SerializedName("album_coverart_100x100") val cover: String
) {


  override fun toString(): String {
    return "Album{" +
        "albumName='" + albumName + '\''.toString() +
        ", albumId=" + albumId +
        '}'.toString()
  }

  override fun equals(other: Any?): Boolean {
    if (other == null || other !is Album) return false
    return albumId == other.albumId
  }

  @RequiresApi(VERSION_CODES.KITKAT)
  override fun hashCode() = Objects.hash(albumId, albumName, cover)
}
