package com.modulstart.mobilehomework.presenters

import com.modulstart.mobilehomework.api.CallbackWrapper
import com.modulstart.mobilehomework.repository.auth.AuthProvider
import com.modulstart.mobilehomework.repository.models.AccessToken
import com.modulstart.mobilehomework.views.signup.SignUpView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter


@InjectViewState
class SignupPresenter (private val provider: AuthProvider) : MvpPresenter<SignUpView>() {

    fun signup(username: String, password: String, name: String) {
        viewState.showProgress()
        provider.signup(username, password, name)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<AccessToken?>(viewState) {
                override fun onSuccess(response: AccessToken?) {
                    viewState.signupSuccess(response!!.token)
                }
            })
    }
}