package com.modulstart.mobilehomework

import android.app.Application
import com.modulstart.mobilehomework.api.TokenInterceptor
import com.modulstart.mobilehomework.di.components.AppComponent
import com.modulstart.mobilehomework.di.components.DaggerAppComponent
import com.modulstart.mobilehomework.di.modules.ApiModule
import javax.inject.Inject

class App : Application() {



    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        daggerInit()
    }



    private fun daggerInit() {
        appComponent = DaggerAppComponent
            .builder()
            .apiModule(ApiModule(this))
            .build()
    }

}