package com.aleksejantonov.lyricseverywhere.api.entities.album

import com.google.gson.annotations.SerializedName

data class AlbumBody(
    @SerializedName("album_list") val albumsContainers: List<AlbumContainer>
)
