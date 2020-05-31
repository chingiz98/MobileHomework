package com.modulstart.mobilehomework.repository.models
import java.math.BigDecimal
import java.util.*

data class Account (

    val id: Long,
    val ownerId: UUID,
    val name: String,
    var amount: BigDecimal,
    val closed: Boolean

)