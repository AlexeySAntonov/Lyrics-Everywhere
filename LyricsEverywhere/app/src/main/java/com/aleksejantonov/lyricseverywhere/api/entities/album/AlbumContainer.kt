package com.aleksejantonov.lyricseverywhere.api.entities.album

import com.google.gson.annotations.SerializedName

data class AlbumContainer(
    @SerializedName("album") val album: Album
)
