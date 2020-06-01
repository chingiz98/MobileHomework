package com.modulstart.mobilehomework.interactors.database

import com.modulstart.mobilehomework.repository.database.AppDatabase
import com.modulstart.mobilehomework.repository.database.TemplateDB
import com.modulstart.mobilehomework.repository.models.Template
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*

class AccountsDatabaseInteractorImpl(val db: AppDatabase) : AccountsDatabaseInteractor {
    override fun getTemplates(fromId: Long): Observable<List<TemplateDB>> {
        return Observable.fromCallable {
            db.templateDato().getAll(fromId)
        }
    }

    override fun saveTemplate(tmp: TemplateDB) {
        Observable.fromCallable {
            db.templateDato().insert(tmp)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }

    override fun deleteTemplate(tmp: TemplateDB) {
        Observable.fromCallable {
            db.templateDato().delete(tmp)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }

}