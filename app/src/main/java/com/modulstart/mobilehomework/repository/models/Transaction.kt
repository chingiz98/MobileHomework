package com.modulstart.mobilehomework.repository.models

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class Transaction (

    val toId: Long,
    val fromId: Long,
    val amount: BigDecimal,
    val comment: String?,
    val type: String,
    val timestamp: Date

)