package com.modulstart.mobilehomework.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class TemplateDB(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val toId: Long,
    val fromId: Long,
    val comment: String,
    val amount: String
)
