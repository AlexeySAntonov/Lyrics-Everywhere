package com.aleksejantonov.lyricseverywhere.api.entities.track

import com.aleksejantonov.lyricseverywhere.api.entities.base_entities.Header
import com.google.gson.annotations.SerializedName

data class TrackMessage(
    @SerializedName("header") val header: Header,
    @SerializedName("body") val body: TrackBody
)
