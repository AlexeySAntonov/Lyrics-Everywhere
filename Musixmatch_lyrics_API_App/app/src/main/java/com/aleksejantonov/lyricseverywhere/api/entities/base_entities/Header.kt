package com.aleksejantonov.lyricseverywhere.api.entities.base_entities

import com.google.gson.annotations.SerializedName

import java.io.Serializable

data class Header(
    @SerializedName("status_code") val statusCode: Int
) : Serializable
