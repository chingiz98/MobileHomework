package com.modulstart.mobilehomework.api.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class AccessTokenDTO (
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("expiration")
    val expires: Date
)