package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.base_entities.Header
import com.google.gson.annotations.SerializedName

data class TrackMessage(
    @SerializedName("header") val header: Header,
    @SerializedName("body") val body: TrackBody
)
