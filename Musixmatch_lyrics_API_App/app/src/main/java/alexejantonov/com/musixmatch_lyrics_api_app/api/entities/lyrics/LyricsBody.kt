package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics

import com.google.gson.annotations.SerializedName

data class LyricsBody(
    @SerializedName("lyrics") val lyrics: Lyrics
)
