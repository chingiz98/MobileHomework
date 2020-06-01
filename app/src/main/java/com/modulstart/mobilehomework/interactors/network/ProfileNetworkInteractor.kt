package com.modulstart.mobilehomework.interactors.network

import android.graphics.Bitmap
import com.modulstart.mobilehomework.api.dto.EmptyResult
import com.modulstart.mobilehomework.repository.models.User
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody


interface ProfileNetworkInteractor {
    fun getProfile() : Observable<User>
    fun uploadPhoto(photo: MultipartBody.Part) : Observable<EmptyResult>
    fun getPhoto() : Observable<Bitmap>
    fun updateInfo(username: String, name: String) : Observable<EmptyResult>
}