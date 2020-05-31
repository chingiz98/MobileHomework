package com.modulstart.mobilehomework.repository.auth

import com.modulstart.mobilehomework.repository.models.AccessToken
import io.reactivex.rxjava3.core.Observable


interface AuthProvider {
    fun logIn(username: String, password: String): Observable<AccessToken>
    fun isLoggedIn() : Boolean
    fun signup(username: String, password: String, name: String): Observable<AccessToken>
}