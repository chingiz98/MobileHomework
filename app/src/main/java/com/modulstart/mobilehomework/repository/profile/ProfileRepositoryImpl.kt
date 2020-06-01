package com.modulstart.mobilehomework.repository.profile

import android.graphics.Bitmap
import com.modulstart.mobilehomework.api.dto.EmptyResult
import com.modulstart.mobilehomework.interactors.memory.ProfileMemoryInteractor
import com.modulstart.mobilehomework.interactors.network.ProfileNetworkInteractor
import com.modulstart.mobilehomework.repository.models.User
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class ProfileRepositoryImpl(val networkInteractor: ProfileNetworkInteractor, val memoryInteractor: ProfileMemoryInteractor) : ProfileRepository {
    override fun getProfile(): Observable<User> {
        val memory = memoryInteractor.getProfile()
        val network = networkInteractor.getProfile().doOnNext {
            memoryInteractor.saveProfile(it)
        }
        return Observable.concat(memory, network)
            .firstElement()
            .toObservable()
    }

    override fun uploadPhoto(photo: MultipartBody.Part): Observable<EmptyResult> {
        return networkInteractor.uploadPhoto(photo)
    }

    override fun getPhoto(): Observable<Bitmap> {
        val memory = memoryInteractor.getPhoto()
        val network = networkInteractor.getPhoto().doOnNext {
            memoryInteractor.savePhoto(it)
        }
        return Observable.concat(memory, network)
            .firstElement()
            .toObservable()
    }

    override fun updateInfo(username: String, name: String): Observable<EmptyResult> {
        return networkInteractor.updateInfo(username, name).doOnNext {
            memoryInteractor.saveNewData(username, name)
        }
    }
}