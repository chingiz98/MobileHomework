package com.modulstart.mobilehomework.repository.models

import java.util.*

data class AccessToken(
    var token: String,
    var expires: Date
)