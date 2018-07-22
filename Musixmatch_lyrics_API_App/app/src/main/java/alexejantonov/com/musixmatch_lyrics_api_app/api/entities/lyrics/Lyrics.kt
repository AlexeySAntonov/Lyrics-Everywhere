package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics

import com.google.gson.annotations.SerializedName

data class Lyrics(
    @SerializedName("lyrics_body") val lyricsText: String
)
