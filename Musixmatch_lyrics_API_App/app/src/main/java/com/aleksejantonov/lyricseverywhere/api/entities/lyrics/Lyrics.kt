package com.aleksejantonov.lyricseverywhere.api.entities.lyrics

import com.google.gson.annotations.SerializedName

data class Lyrics(
    @SerializedName("lyrics_body") val lyricsText: String
)
