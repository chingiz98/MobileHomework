package com.modulstart.mobilehomework.interactors.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.modulstart.mobilehomework.api.Api
import com.modulstart.mobilehomework.api.dto.EmptyResult
import com.modulstart.mobilehomework.repository.models.User
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class ProfileNetworkInteractorImpl(val api: Api) : ProfileNetworkInteractor {
    override fun getProfile(): Observable<User> {
        return api.getProfile().map { userDto -> User(userDto.username, userDto.name, userDto.id, userDto.status) }
    }

    override fun uploadPhoto(photo: MultipartBody.Part) : Observable<EmptyResult> {
        return api.uploadImage(photo)
    }

    override fun getPhoto(): Observable<Bitmap> {
        return api.getImage().map { BitmapFactory.decodeStream(it.byteStream()) }
    }
}