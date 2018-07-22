package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist

import com.google.gson.annotations.SerializedName

data class ArtistBody(
    @SerializedName("artist_list") val artistContainers: List<ArtistContainer>
)
