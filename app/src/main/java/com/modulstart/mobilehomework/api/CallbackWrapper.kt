package com.modulstart.mobilehomework.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.modulstart.mobilehomework.api.dto.ApiError
import com.modulstart.mobilehomework.views.base.BaseView
import io.reactivex.rxjava3.observers.DisposableObserver
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException


abstract class CallbackWrapper<T>(view: BaseView) : DisposableObserver<T?>() {

    private val weakReference: WeakReference<BaseView?>?

    protected abstract fun onSuccess(response: T?)
    override fun onNext(response: T?) {
        onSuccess(response)
    }

    override fun onError(e: Throwable?) {
        val view: BaseView? = weakReference?.get()
        when (e) {
            is HttpException -> {
                val httpException = (e as HttpException?)

                val gson = GsonBuilder().create()
                lateinit var mError : ApiError
                try {
                    mError = gson.fromJson(httpException?.response()?.errorBody()?.string(), ApiError::class.java)
                    view?.showError(mError)
                } catch (e: Exception) {

                }
                if(httpException?.code() == 401){
                    view?.logout()
                }

            }
            is SocketTimeoutException -> {
                Log.d("CallbackWrapper", "SOCKET_TIMEOUT")
            }
            is IOException -> {
                Log.d("CallbackWrapper", "IO_EXCEPTION")
            }
            else -> {
                Log.d("CallbackWrapper", "OTHER_ERROR")
            }
        }
    }

    override fun onComplete() {}
    private fun getErrorMessage(responseBody: ResponseBody?): String? {
        return try {
            val jsonObject = JSONObject(responseBody!!.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message
        }
    }

    init {
        weakReference = WeakReference(view)
    }
}