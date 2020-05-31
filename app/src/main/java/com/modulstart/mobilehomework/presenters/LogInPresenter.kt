package com.modulstart.mobilehomework.presenters

import android.util.Log
import com.modulstart.mobilehomework.api.CallbackWrapper
import com.modulstart.mobilehomework.repository.auth.AuthProvider
import com.modulstart.mobilehomework.repository.models.AccessToken
import com.modulstart.mobilehomework.views.login.LogInView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter


@InjectViewState
class LogInPresenter (private val provider: AuthProvider) : MvpPresenter<LogInView>() {

    fun checkLogin() {
        if(provider.isLoggedIn())
            viewState.login()
    }

    fun logIn(username: String, password: String) {
        viewState.showProgress()
        provider.logIn(username, password)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<AccessToken?>(viewState) {
                override fun onSuccess(response: AccessToken?) {
                    Log.d("LOG", "LOGIN_SUCCESS")
                    viewState.loginSuccess(response!!.token)
                }
            })
    }


}