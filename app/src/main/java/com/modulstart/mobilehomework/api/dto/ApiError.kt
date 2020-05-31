package com.modulstart.mobilehomework.api.dto

import com.google.gson.annotations.SerializedName

data class ApiError (
    @SerializedName("Code")
    val code: Int,

    @SerializedName("Message")
    val message: String
)