package com.modulstart.mobilehomework.repository.accounts

import com.modulstart.mobilehomework.api.dto.EmptyResult
import com.modulstart.mobilehomework.interactors.memory.AccountsMemoryInteractor
import com.modulstart.mobilehomework.interactors.network.AccountsNetworkInteractor
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.Transaction
import io.reactivex.rxjava3.core.Observable
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class AccountsRepositoryImpl(val networkInteractor: AccountsNetworkInteractor, val memoryInteractor: AccountsMemoryInteractor) :
    AccountsRepository {

    override fun getAccounts(): Observable<MutableList<Account>> {

        val memory = memoryInteractor.getAccounts()
        val network = networkInteractor.getAccounts().doOnNext {
            memoryInteractor.saveAccounts(it)
        }

        return Observable.concat(memory, network)
            .firstElement()
            .toObservable()
    }

    override fun getAccountById(id: Long): Observable<Account> {
        val memory = memoryInteractor.getAccountById(id)
        val network = networkInteractor.getAccountById(id)

        return Observable.concat(memory, network)
            .firstElement()
            .toObservable()
    }

    override fun makeDeposit(toId: Long, amount: BigDecimal): Observable<EmptyResult> {
        return networkInteractor.makeDeposit(toId, amount).doOnNext {
            memoryInteractor.saveDeposit(toId, amount)
            memoryInteractor.saveTransaction(Transaction(toId, 0, amount, "", "deposit", Calendar.getInstance().time))
        }
    }

    override fun makeTransaction(
        fromId: Long,
        toId: Long,
        amount: BigDecimal,
        comment: String
    ): Observable<EmptyResult> {
        return networkInteractor.makeTransaction(fromId, toId, amount, comment).doOnNext {
            memoryInteractor.saveTransaction(Transaction(toId, fromId, amount, comment, "transaction", Calendar.getInstance().time))
        }
    }

    override fun getAllTransactions(): Observable<MutableList<Transaction>> {
        val memory = memoryInteractor.getAllTransactions()
        val network = networkInteractor.getAllTransactions().doOnNext {
            memoryInteractor.saveTransactions(it)
        }
        return Observable.concat(memory, network)
            .firstElement()
            .toObservable()
    }

    override fun createAccount(name: String): Observable<Account> {
        return networkInteractor.createAccount(name).doOnNext {
            memoryInteractor.saveCreatedAccount(it)
        }
    }

    override fun closeAccount(id: Long): Observable<Account> {
        return networkInteractor.closeAccount(id).doOnNext {
            memoryInteractor.closeAccount(id)
        }
    }
}