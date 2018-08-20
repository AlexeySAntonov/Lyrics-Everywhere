package com.aleksejantonov.lyricseverywhere.api.entities.lyrics

import com.google.gson.annotations.SerializedName

data class LyricsBody(
    @SerializedName("lyrics") val lyrics: Lyrics
)
