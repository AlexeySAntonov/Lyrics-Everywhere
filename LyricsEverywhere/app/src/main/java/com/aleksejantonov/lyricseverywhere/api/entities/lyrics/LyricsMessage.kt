package com.aleksejantonov.lyricseverywhere.api.entities.lyrics

import com.aleksejantonov.lyricseverywhere.api.entities.base_entities.Header
import com.google.gson.annotations.SerializedName

data class LyricsMessage(
    @SerializedName("header") val header: Header,
    @SerializedName("body") val body: LyricsBody
)
