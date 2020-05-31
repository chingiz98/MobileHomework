package com.modulstart.mobilehomework.interactors.memory

import android.graphics.Bitmap
import com.modulstart.mobilehomework.repository.models.User
import io.reactivex.rxjava3.core.Observable

interface ProfileMemoryInteractor {
    fun getProfile() : Observable<User>
    fun saveProfile(user: User)
    fun getPhoto() : Observable<Bitmap>
    fun savePhoto(bitmap: Bitmap)
}