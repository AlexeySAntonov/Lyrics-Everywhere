package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("message") val message: TrackMessage
)
