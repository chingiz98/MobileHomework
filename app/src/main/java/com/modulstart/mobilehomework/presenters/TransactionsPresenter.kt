package com.modulstart.mobilehomework.presenters

import android.util.Log
import com.modulstart.mobilehomework.api.CallbackWrapper
import com.modulstart.mobilehomework.repository.accounts.AccountsRepository
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.Transaction
import com.modulstart.mobilehomework.views.transactions.TransactionsView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction

class TransactionsPresenter(private val repository: AccountsRepository) : MvpPresenter<TransactionsView>() {

    lateinit var transactions: MutableList<Transaction>
    lateinit var accounts: MutableList<Account>
    var start: Long = 0L
    var end: Long = 0L

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadTransactions()
    }

    private fun combine(firstResponse: MutableList<Transaction>, secondResponse: MutableList<Account>){
        //viewState.showData(firstResponse, secondResponse)
        transactions = firstResponse
        accounts = secondResponse
    }

    private fun loadTransactions() {
        Observable.zip(
            repository.getAllTransactions().subscribeOn(Schedulers.newThread()),
            repository.getAccounts().subscribeOn(Schedulers.newThread()),
            BiFunction{
                    firstResponse: MutableList<Transaction>,
                    secondResponse: MutableList<Account> ->
                    combine(firstResponse, secondResponse)
                 })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<Unit?>(viewState) {
                override fun onSuccess(response: Unit?) {
                    Log.d("MSG", "UNIT")
                    showFiltered(start, end)
                    //viewState.showData(transactions, accounts)
                    //viewState.showTransactions(response!!)
                }
            })
    }

    fun showFiltered(start: Long, end: Long){

        if(this::transactions.isInitialized && this::accounts.isInitialized){
            if(start == 0L || end == 0L){
                viewState.showData(transactions, accounts)
                return
            }
            this.start = start
            this.end = end
            transactions = transactions.filter { it.timestamp.time >= start &&                                                                                                                                     it.timestamp.time <= end }.toMutableList()
            viewState.showData(transactions, accounts)
        }
    }

}