package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track

import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

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

  override fun equals(o: Any?): Boolean {
    if (o !is Track) {
      return false
    }
    val track = o as Track?
    return artistId == track!!.artistId
  }
}
