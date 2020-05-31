package com.modulstart.mobilehomework.interactors.memory

import android.graphics.Bitmap
import com.modulstart.mobilehomework.repository.models.User
import io.reactivex.rxjava3.core.Observable

class ProfileMemoryInteractorImpl : ProfileMemoryInteractor {
    private lateinit var user: User
    private lateinit var image: Bitmap

    override fun getProfile(): Observable<User> {
        return Observable.create{
            if(this::user.isInitialized){
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
            if(this::image.isInitialized){
                it.onNext(image)
            }
            it.onComplete()
        }
    }

    override fun savePhoto(bitmap: Bitmap) {
        this.image = bitmap
    }
}