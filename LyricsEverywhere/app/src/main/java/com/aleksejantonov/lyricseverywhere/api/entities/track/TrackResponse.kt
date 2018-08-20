package com.aleksejantonov.lyricseverywhere.api.entities.track

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("message") val message: TrackMessage
)
