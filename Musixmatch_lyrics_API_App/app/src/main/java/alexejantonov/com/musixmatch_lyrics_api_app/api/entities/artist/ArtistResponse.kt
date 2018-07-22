package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist

import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("message") val message: ArtistMessage
)
