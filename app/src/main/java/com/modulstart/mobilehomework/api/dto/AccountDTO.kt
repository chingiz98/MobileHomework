package com.modulstart.mobilehomework.api.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

data class AccountDTO (
    @SerializedName("id")
    val id: Long,

    @SerializedName("owner_id")
    val ownerId: UUID,

    @SerializedName("name")
    val name: String,


    @SerializedName("amount")
    val amount: BigDecimal,


    @SerializedName("closed")
    val closed: Boolean


)