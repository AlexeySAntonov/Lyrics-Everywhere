package com.aleksejantonov.lyricseverywhere.api.entities.artist

import com.aleksejantonov.lyricseverywhere.api.entities.base_entities.Header
import com.google.gson.annotations.SerializedName

data class ArtistMessage(
    @SerializedName("header") val header: Header,
    @SerializedName("body") val body: ArtistBody
)
