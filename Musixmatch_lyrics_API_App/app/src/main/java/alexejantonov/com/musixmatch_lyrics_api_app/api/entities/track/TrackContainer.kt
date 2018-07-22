package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track

import com.google.gson.annotations.SerializedName

data class TrackContainer(
    @SerializedName("track") val track: Track
)
