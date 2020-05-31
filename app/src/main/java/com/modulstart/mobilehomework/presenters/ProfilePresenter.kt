package com.modulstart.mobilehomework.presenters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.modulstart.mobilehomework.api.CallbackWrapper
import com.modulstart.mobilehomework.api.dto.EmptyResult
import com.modulstart.mobilehomework.repository.models.User
import com.modulstart.mobilehomework.repository.profile.ProfileRepository
import com.modulstart.mobilehomework.views.profile.ProfileView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.File


class ProfilePresenter(private val repository: ProfileRepository) : MvpPresenter<ProfileView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getProfile()
        getPhoto()
    }

    private fun getProfile() {
        viewState.showLoading()
        repository.getProfile()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<User?>(viewState) {
                override fun onSuccess(response: User?) {
                    viewState.showProfile(response!!)
                }
            })
    }

    fun uploadPhoto(image: File){
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image)

        val body = MultipartBody.Part.createFormData("file", image.name, requestFile)
        repository.uploadPhoto(body)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<EmptyResult?>(viewState) {
                override fun onSuccess(response: EmptyResult?) {
                }
            })
    }

    private fun getPhoto(){
        repository.getPhoto()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackWrapper<Bitmap?>(viewState) {
                override fun onSuccess(response: Bitmap?) {
                    viewState.showProfileImage(response!!)
                }

                override fun onError(e: Throwable?) {
                    if(!(e is HttpException && (e as HttpException).code() == 500))
                        super.onError(e)
                }
            })
    }

    fun logout() {
        viewState.logout()
    }
}