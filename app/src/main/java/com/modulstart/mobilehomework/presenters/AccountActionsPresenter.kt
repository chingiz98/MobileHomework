package com.modulstart.mobilehomework.presenters

import android.util.Log
import com.modulstart.mobilehomework.api.CallbackWrapper
import com.modulstart.mobilehomework.api.dto.EmptyResult
import com.modulstart.mobilehomework.repository.accounts.AccountsRepository
import com.modulstart.mobilehomework.repository.database.TemplateDB
import com.modulstart.mobilehomework.repository.models.Account
import com.modulstart.mobilehomework.repository.models.User
import com.modulstart.mobilehomework.repository.profile.ProfileRepository
import com.modulstart.mobilehomework.views.accounts.actions.AccountActionsView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter
import java.math.BigDecimal
import java.util.*

class AccountActionsPresenter(private val repository: AccountsRepository, private val profileRepository: ProfileRepository, private val accountId: Long):
    MvpPresenter<AccountActionsView>(){
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadAccount()
    }

    private fun loadAccount() {
        viewState.showLoading()
        repository.getAccountById(accountId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<Account?>(viewState) {
                override fun onSuccess(response: Account?) {
                    viewState.showAccount(response!!)
                }
            })
    }

    fun loadAccounts(){
        repository.getAccounts()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<MutableList<Account>?>(viewState) {
                override fun onSuccess(response: MutableList<Account>?) {
                    viewState.showTransferDialog(response!!)
                }
            })
    }

    fun makeDeposit(accountId: Long, amount: BigDecimal){
        repository.makeDeposit(accountId, amount)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<EmptyResult?>(viewState) {
                override fun onSuccess(response: EmptyResult?) {
                    viewState.depositSuccess(amount)
                }
            })
    }

    fun makeTransaction(fromId: Long, toId: Long, amount: BigDecimal, comment: String){
        repository.makeTransaction(fromId, toId, amount, comment)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<EmptyResult?>(viewState) {
                override fun onSuccess(response: EmptyResult?) {
                    viewState.transactionSuccess(fromId, toId, comment, amount)
                }
            })
    }

    fun closeAccount(id: Long){
        repository.closeAccount(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<Account?>(viewState) {
                override fun onSuccess(response: Account?) {
                    viewState.accountCloseSuccess()
                }
            })
    }

    fun loadTemplates(fromId: Long){
        repository.getTemplates(fromId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<List<TemplateDB>?>(viewState) {
                override fun onSuccess(response: List<TemplateDB>?) {
                    viewState.showTemplatesDialog(response!!)
                }
            })
    }

    fun saveTemplate(tmp: TemplateDB){
        repository.saveTemplate(tmp)
    }

    fun deleteTemplate(tmp: TemplateDB){
        repository.deleteTemplate(tmp)
    }





}