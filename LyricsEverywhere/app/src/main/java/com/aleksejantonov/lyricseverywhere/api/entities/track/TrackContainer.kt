package com.aleksejantonov.lyricseverywhere.api.entities.track

import com.google.gson.annotations.SerializedName

data class TrackContainer(
    @SerializedName("track") val track: Track
)
