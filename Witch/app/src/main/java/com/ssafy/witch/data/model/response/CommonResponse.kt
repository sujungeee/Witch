package com.ssafy.witch.data.model.response

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: JsonElement?,
    @SerializedName("error") val error: ErrorResponse
)
