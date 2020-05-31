package com.modulstart.mobilehomework.interactors.memory

import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.Transaction
import io.reactivex.rxjava3.core.Observable
import java.math.BigDecimal

interface AccountsMemoryInteractor : BaseMemoryInteractor {
    fun getAccounts() : Observable<MutableList<Account>>
    fun saveAccounts(accounts: MutableList<Account>)
    fun getAccountById(id: Long) : Observable<Account>
    fun saveDeposit(toId: Long, amount: BigDecimal)
    fun saveTransaction(transaction: Transaction)
    fun saveTransactions(transactions: MutableList<Transaction>)
    fun getAllTransactions() : Observable<MutableList<Transaction>>
    fun saveCreatedAccount(account: Account)
    fun closeAccount(id: Long)
}