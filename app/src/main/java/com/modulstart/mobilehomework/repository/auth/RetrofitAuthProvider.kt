package com.modulstart.mobilehomework.repository.auth

import android.content.SharedPreferences
import com.modulstart.mobilehomework.api.Api
import com.modulstart.mobilehomework.api.dto.LoginCredentialsDTO
import com.modulstart.mobilehomework.api.dto.SignUpCredentialsDTO
import com.modulstart.mobilehomework.repository.auth.AuthProvider
import com.modulstart.mobilehomework.repository.models.AccessToken
import io.reactivex.rxjava3.core.Observable


class RetrofitAuthProvider(private val api: Api, private val sharedPrefs: SharedPreferences) :
    AuthProvider {


    override fun logIn(username: String, password: String): Observable<AccessToken> {
        return api.logIn(LoginCredentialsDTO(username, password)).map{ AccessToken(it.accessToken, it.expires) }
    }

    override fun isLoggedIn(): Boolean {
        return sharedPrefs.getBoolean("logged_in", false)
    }

    override fun signup(username: String, password: String, name: String): Observable<AccessToken> {
        return api.signup(SignUpCredentialsDTO(username, password, name)).map { AccessToken(it.accessToken, it.expires) }
    }

}