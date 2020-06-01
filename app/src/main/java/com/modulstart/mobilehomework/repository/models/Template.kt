package com.modulstart.mobilehomework.repository.models

import java.math.BigDecimal

data class Template (
    val toId: Long,
    val fromId: Long,
    val amount: BigDecimal,
    val comment: String?
    )