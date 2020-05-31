package com.modulstart.mobilehomework.repository.profile


import android.graphics.Bitmap
import com.modulstart.mobilehomework.api.dto.EmptyResult
import com.modulstart.mobilehomework.repository.models.User
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface ProfileRepository {
    fun getProfile(): Observable<User>
    fun uploadPhoto(photo: MultipartBody.Part) : Observable<EmptyResult>
    fun getPhoto() : Observable<Bitmap>
}