package com.modulstart.mobilehomework.repository.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class User (
    var username: String,
    var name: String,
    var id: UUID,
    var status: String
)