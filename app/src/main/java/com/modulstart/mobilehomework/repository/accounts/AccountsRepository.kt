package com.modulstart.mobilehomework.repository.accounts

import com.modulstart.mobilehomework.api.dto.EmptyResult
import com.modulstart.mobilehomework.repository.database.TemplateDB
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.Transaction
import io.reactivex.rxjava3.core.Observable
import java.math.BigDecimal
import java.util.*

interface AccountsRepository {
    fun getAccounts() : Observable<MutableList<Account>>
    fun getAccountById(id: Long) : Observable<Account>
    fun makeDeposit(toId: Long, amount: BigDecimal) : Observable<EmptyResult>
    fun makeTransaction(fromId: Long, toId: Long, amount: BigDecimal, comment: String) : Observable<EmptyResult>
    fun getAllTransactions() : Observable<MutableList<Transaction>>
    fun createAccount(name: String) : Observable<Account>
    fun closeAccount(id: Long) : Observable<Account>
    fun getTemplates(fromId: Long) : Observable<List<TemplateDB>>
    fun saveTemplate(tmp: TemplateDB)
    fun deleteTemplate(tmp: TemplateDB)
}