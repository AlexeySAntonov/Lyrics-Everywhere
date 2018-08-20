package com.aleksejantonov.lyricseverywhere.api.entities.artist

import com.google.gson.annotations.SerializedName

data class ArtistBody(
    @SerializedName("artist_list") val artistContainers: List<ArtistContainer>
)
