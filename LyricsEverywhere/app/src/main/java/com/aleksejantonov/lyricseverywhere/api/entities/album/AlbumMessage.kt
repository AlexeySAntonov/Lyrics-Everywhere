package com.aleksejantonov.lyricseverywhere.api.entities.album

import com.aleksejantonov.lyricseverywhere.api.entities.base_entities.Header
import com.google.gson.annotations.SerializedName

data class AlbumMessage(
    @SerializedName("header") val header: Header,
    @SerializedName("body") val body: AlbumBody
)
