package com.modulstart.mobilehomework.repository.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class User (
    val username: String,
    val name: String,
    val id: UUID,
    val status: String
)