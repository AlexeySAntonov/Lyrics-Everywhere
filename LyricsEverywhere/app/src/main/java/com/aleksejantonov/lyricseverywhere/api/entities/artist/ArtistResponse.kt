package com.aleksejantonov.lyricseverywhere.api.entities.artist

import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("message") val message: ArtistMessage
)
