package com.modulstart.mobilehomework.api.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

data class UserDTO (
    @SerializedName("username")
    val username: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("id")
    val id: UUID,

    @SerializedName("status")
    val status: String

)