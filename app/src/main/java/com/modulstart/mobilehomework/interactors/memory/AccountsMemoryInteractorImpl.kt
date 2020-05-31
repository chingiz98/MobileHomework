package com.modulstart.mobilehomework.interactors.memory

import android.util.Log
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.Transaction
import io.reactivex.rxjava3.core.Observable
import java.math.BigDecimal

class AccountsMemoryInteractorImpl :
    AccountsMemoryInteractor {

    private var accounts: MutableList<Account>? = null
    private var transactions: MutableList<Transaction>? = null

    override fun getAccounts(): Observable<MutableList<Account>> {
        return Observable.create{ acc ->
            accounts?.let {
                acc.onNext(accounts)
            }
            acc.onComplete()
        }
    }

    override fun saveAccounts(accounts: MutableList<Account>) {
        this.accounts = accounts.toMutableList()
    }

    override fun getAccountById(id: Long): Observable<Account> {
        return Observable.create{
            val accForSearch = accounts?.find { acc -> acc.id == id }
            if(accForSearch != null){
                it.onNext(accForSearch)
            }
            it.onComplete()
        }
    }

    override fun saveDeposit(toId: Long, amount: BigDecimal) {
        accounts?.let {
            val index = it.indexOf(it.find { it.id == toId })
            //accounts[index].amount += amount
        }
    }

    override fun saveTransaction(transaction: Transaction) {
        if(accounts != null){
            val indexTo = accounts?.indexOf(accounts?.find { it.id == transaction.toId })
            accounts!![indexTo!!].amount += transaction.amount
            if(transaction.fromId != 0L){
                val indexFrom = accounts?.indexOf(accounts?.find { it.id == transaction.fromId })
                accounts!![indexFrom!!].amount -= transaction.amount
            }
        }
        if(transactions != null){
            transactions?.add(0, transaction)
        }
    }

    override fun saveTransactions(transactions: MutableList<Transaction>) {
        this.transactions = transactions.toMutableList()
    }

    override fun getAllTransactions(): Observable<MutableList<Transaction>> {
        return Observable.create{
            if(transactions != null){
                it.onNext(transactions)
            }
            it.onComplete()
        }
    }

    override fun saveCreatedAccount(account: Account) {
        if(accounts != null){
            accounts?.add(0, account)
        }
    }

    override fun closeAccount(id: Long) {
        if(accounts != null){
            val index = accounts?.indexOf(accounts?.find { it.id == id })
            accounts?.removeAt(index!!)
        }
    }

    override fun clear() {
        accounts = null
        transactions = null
    }

}