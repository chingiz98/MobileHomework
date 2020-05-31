package com.modulstart.mobilehomework.interactors.memory

import android.util.Log
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.Transaction
import io.reactivex.rxjava3.core.Observable
import java.math.BigDecimal

class AccountsMemoryInteractorImpl :
    AccountsMemoryInteractor {

    private lateinit var accounts: MutableList<Account>
    private lateinit var transactions: MutableList<Transaction>

    override fun getAccounts(): Observable<MutableList<Account>> {
        return Observable.create{
            if(this::accounts.isInitialized){
                it.onNext(accounts)
            }
            it.onComplete()
        }
    }

    override fun saveAccounts(accounts: MutableList<Account>) {
        this.accounts = accounts.toMutableList()
    }

    override fun getAccountById(id: Long): Observable<Account> {
        return Observable.create{
            val accForSearch = accounts.find { acc -> acc.id == id }
            if(accForSearch != null){
                it.onNext(accForSearch)
            }
            it.onComplete()
        }
    }

    override fun saveDeposit(toId: Long, amount: BigDecimal) {
        if(this::accounts.isInitialized){
            val index = accounts.indexOf(accounts.find { it.id == toId })
            //accounts[index].amount += amount
        }
    }

    override fun saveTransaction(transaction: Transaction) {
        if(this::accounts.isInitialized){
            val indexTo = accounts.indexOf(accounts.find { it.id == transaction.toId })
            accounts[indexTo].amount += transaction.amount
            if(transaction.fromId != 0L){
                val indexFrom = accounts.indexOf(accounts.find { it.id == transaction.fromId })
                accounts[indexFrom].amount -= transaction.amount
            }
        }
        if(this::transactions.isInitialized){
            transactions.add(0, transaction)
        }
    }

    override fun saveTransactions(transactions: MutableList<Transaction>) {
        this.transactions = transactions.toMutableList()
    }

    override fun getAllTransactions(): Observable<MutableList<Transaction>> {
        return Observable.create{
            if(this::transactions.isInitialized){
                it.onNext(transactions)
            }
            it.onComplete()
        }
    }

    override fun saveCreatedAccount(account: Account) {
        if(this::accounts.isInitialized){
            accounts.add(0, account)
        }
    }

    override fun closeAccount(id: Long) {
        if(this::accounts.isInitialized){
            val index = accounts.indexOf(accounts.find { it.id == id })
            accounts.removeAt(index)
        }
    }

}