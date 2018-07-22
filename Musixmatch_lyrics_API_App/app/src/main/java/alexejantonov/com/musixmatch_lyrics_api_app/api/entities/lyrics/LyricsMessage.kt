package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.base_entities.Header
import com.google.gson.annotations.SerializedName

data class LyricsMessage(
    @SerializedName("header") val header: Header,
    @SerializedName("body") val body: LyricsBody
)
