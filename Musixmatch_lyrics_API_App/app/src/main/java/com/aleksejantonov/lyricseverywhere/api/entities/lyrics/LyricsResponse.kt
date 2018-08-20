package com.aleksejantonov.lyricseverywhere.api.entities.lyrics

import com.google.gson.annotations.SerializedName

data class LyricsResponse(
    @SerializedName("message") val message: LyricsMessage
)
