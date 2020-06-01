package com.modulstart.mobilehomework.interactors.memory

import android.graphics.Bitmap
import com.modulstart.mobilehomework.repository.models.User
import io.reactivex.rxjava3.core.Observable

class ProfileMemoryInteractorImpl : ProfileMemoryInteractor {
    private var user: User? = null
    private var image: Bitmap? = null

    override fun getProfile(): Observable<User> {
        return Observable.create{
            if(user != null){
                it.onNext(user)
            }
            it.onComplete()
        }
    }

    override fun saveProfile(user: User) {
        this.user = user
    }

    override fun getPhoto(): Observable<Bitmap> {
        return Observable.create{
            if(image != null){
                it.onNext(image)
            }
            it.onComplete()
        }
    }

    override fun savePhoto(bitmap: Bitmap) {
        this.image = bitmap
    }

    override fun saveNewData(username: String, name: String) {
        if(user != null){
            user!!.username = username
            user!!.name = name
        }

    }

    override fun clear() {
        user = null
        image = null
    }
}