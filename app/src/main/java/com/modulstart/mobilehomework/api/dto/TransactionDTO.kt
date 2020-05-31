package com.modulstart.mobilehomework.api.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class TransactionDTO (

    @SerializedName("to_id")
    val toId: Long,

    @SerializedName("from_id")
    val fromId: Long,

    @SerializedName("amount")
    val amount: BigDecimal,

    @SerializedName("comment")
    val comment: String?,


    @SerializedName("type")
    val type: String,

    @SerializedName("timestamp")
    val timestamp: Date



)