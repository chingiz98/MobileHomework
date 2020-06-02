package com.modulstart.mobilehomework.interactors.database

import com.modulstart.mobilehomework.repository.database.TemplateDB
import com.modulstart.mobilehomework.repository.models.Template
import io.reactivex.rxjava3.core.Observable
import java.util.*

interface AccountsDatabaseInteractor {
    fun getTemplates(fromId: Long) : Observable<List<TemplateDB>>
    fun saveTemplate(tmp: TemplateDB)
    fun deleteTemplate(tmp: TemplateDB)
}