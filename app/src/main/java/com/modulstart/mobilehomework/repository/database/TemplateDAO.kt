package com.modulstart.mobilehomework.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface TemplateDao {
    @Query("SELECT * FROM templatedb WHERE fromId = :queryId")
    fun getAll(queryId: Long): List<TemplateDB>

    @Insert
    fun insert(template: TemplateDB?)

    @Delete
    fun delete(template: TemplateDB?)

    @Query("DELETE FROM templatedb")
    fun deleteAll()
}