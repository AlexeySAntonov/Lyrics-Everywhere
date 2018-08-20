package com.aleksejantonov.lyricseverywhere.api.entities.track

import com.google.gson.annotations.SerializedName

data class TrackBody(
    @SerializedName("track_list") val trackContainers: List<TrackContainer>
)
