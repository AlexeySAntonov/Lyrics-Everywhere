package com.aleksejantonov.lyricseverywhere.api.entities.album

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("message") val message: AlbumMessage
)
