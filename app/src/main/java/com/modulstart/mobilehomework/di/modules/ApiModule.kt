package com.modulstart.mobilehomework.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.modulstart.mobilehomework.App
import com.modulstart.mobilehomework.api.Api
import com.modulstart.mobilehomework.api.DateDeserializer
import com.modulstart.mobilehomework.api.TokenInterceptor
import com.modulstart.mobilehomework.interactors.memory.AccountsMemoryInteractor
import com.modulstart.mobilehomework.interactors.memory.AccountsMemoryInteractorImpl
import com.modulstart.mobilehomework.interactors.memory.ProfileMemoryInteractor
import com.modulstart.mobilehomework.interactors.memory.ProfileMemoryInteractorImpl
import com.modulstart.mobilehomework.interactors.network.AccountsNetworkInteractor
import com.modulstart.mobilehomework.interactors.network.AccountsNetworkInteractorImpl
import com.modulstart.mobilehomework.interactors.network.ProfileNetworkInteractor
import com.modulstart.mobilehomework.interactors.network.ProfileNetworkInteractorImpl
import com.modulstart.mobilehomework.repository.accounts.AccountsRepository
import com.modulstart.mobilehomework.repository.accounts.AccountsRepositoryImpl
import com.modulstart.mobilehomework.repository.auth.AuthProvider
import com.modulstart.mobilehomework.repository.auth.RetrofitAuthProvider
import com.modulstart.mobilehomework.repository.profile.ProfileRepository
import com.modulstart.mobilehomework.repository.profile.ProfileRepositoryImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton


@Module
class ApiModule (var app: App) {

    @Provides
    @Singleton
    fun provideApp(): App = app

    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideOkhttp(tokenInterceptor: TokenInterceptor): OkHttpClient {
        val okhttpBuilder = OkHttpClient().newBuilder().addInterceptor(tokenInterceptor)
        return okhttpBuilder.build()
    }

    @Provides
    fun provideApi(okHttpClient: OkHttpClient) : Api {

        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Date::class.java, DateDeserializer())
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .client(okHttpClient)
            .build()
        return retrofit.create(Api::class.java)
    }

    @Provides
    fun provideAuthDataProvider(api : Api, prefs: SharedPreferences) : AuthProvider {
        return RetrofitAuthProvider(
            api,
            prefs
        )
    }

    @Provides
    fun provideAccountsNetworkInteractor(api: Api) : AccountsNetworkInteractor {
        return AccountsNetworkInteractorImpl(
            api
        )
    }

    @Provides
    @Singleton
    fun provideAccountsMemoryInteractor() : AccountsMemoryInteractor {
        return AccountsMemoryInteractorImpl()
    }


    @Provides
    @Singleton
    fun provideAccountsRepository(networkInteractor: AccountsNetworkInteractor, memoryInteractor: AccountsMemoryInteractor) : AccountsRepository {
        return AccountsRepositoryImpl(
            networkInteractor,
            memoryInteractor
        )
    }

    @Provides
    fun provideProfileNetworkInteractor(api: Api) : ProfileNetworkInteractor {
        return ProfileNetworkInteractorImpl(
            api
        )
    }

    @Provides
    @Singleton
    fun provideProfileMemoryInteractor() : ProfileMemoryInteractor {
        return ProfileMemoryInteractorImpl()
    }


    @Provides
    @Singleton
    fun provideProfileRepository(networkInteractor: ProfileNetworkInteractor, memoryInteractor: ProfileMemoryInteractor) : ProfileRepository{
        return ProfileRepositoryImpl(networkInteractor, memoryInteractor)
    }

}