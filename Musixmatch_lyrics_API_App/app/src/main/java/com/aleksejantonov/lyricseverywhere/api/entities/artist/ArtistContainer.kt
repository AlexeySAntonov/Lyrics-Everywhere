package com.aleksejantonov.lyricseverywhere.api.entities.artist

import com.google.gson.annotations.SerializedName

data class ArtistContainer(
    @SerializedName("artist") val artist: Artist
)
