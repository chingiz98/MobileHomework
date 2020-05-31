package com.modulstart.mobilehomework.presenters

import android.util.Log
import com.modulstart.mobilehomework.api.CallbackWrapper
import com.modulstart.mobilehomework.repository.accounts.AccountsRepository
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.views.accounts.accountsList.AccountsView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter

class AccountsPresenter (private val repository: AccountsRepository) : MvpPresenter<AccountsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadAccounts()
    }

    private fun loadAccounts() {
        viewState.showAccountsLoading()
        repository.getAccounts()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<MutableList<Account>?>(viewState) {
                override fun onSuccess(response: MutableList<Account>?) {
                    viewState.showAccounts(response!!)
                }
            })
    }

    fun createAccount(name: String){
        repository.createAccount(name)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<Account?>(viewState) {
                override fun onSuccess(response: Account?) {
                    viewState.createAccountSuccess(response!!)
                    //loadAccounts()
                }
            })
    }

}