package com.modulstart.mobilehomework.interactors.network

import com.modulstart.mobilehomework.api.Api
import com.modulstart.mobilehomework.api.dto.EmptyResult
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.Transaction
import io.reactivex.rxjava3.core.Observable
import java.math.BigDecimal

class AccountsNetworkInteractorImpl (val api: Api) :
    AccountsNetworkInteractor {
    override fun getAccounts(): Observable<MutableList<Account>> {
        return api.getAccounts()
            .map{ dtoList -> dtoList.map { dto ->  Account(dto.id, dto.ownerId, dto.name, dto.amount, dto.closed) }.toMutableList()}
    }

    override fun getAccountById(id: Long): Observable<Account> {
        return api.getAccountById(id).map { dto -> Account(dto.id, dto.ownerId, dto.name, dto.amount, dto.closed) }
    }

    override fun makeDeposit(toId: Long, amount: BigDecimal): Observable<EmptyResult> {

        return api.makeDeposit(toId, amount)
    }

    override fun makeTransaction(fromId: Long, toId: Long, amount: BigDecimal, comment: String): Observable<EmptyResult> {
        return api.makeTransaction(fromId, toId, amount, comment)
    }

    override fun getAllTransactions(): Observable<MutableList<Transaction>> {
        return api.getAllTransactions().map { trList -> trList.map { tr -> Transaction(tr.toId, tr.fromId, tr.amount, tr.comment, tr.type, tr.timestamp) }.toMutableList() }
    }

    override fun createAccount(name: String): Observable<Account> {
        return api.createAccount(name).map { acc -> Account(acc.id, acc.ownerId, acc.name, acc.amount, acc.closed) }
    }

    override fun closeAccount(id: Long): Observable<Account> {
        return api.closeAccount(id).map { acc -> Account(acc.id, acc.ownerId, acc.name, acc.amount, acc.closed) }
    }
}