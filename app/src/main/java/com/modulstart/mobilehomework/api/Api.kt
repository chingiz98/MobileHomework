package com.modulstart.mobilehomework.api

import com.modulstart.mobilehomework.api.dto.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.math.BigDecimal


interface Api {

    @POST("/auth/login")
    @Headers("Content-Type: application/json")
    fun logIn(@Body loginCredentialsDTO: LoginCredentialsDTO): Observable<AccessTokenDTO>

    @GET("/accounts/getAccounts")
    @Headers("Content-Type: application/json")
    fun getAccounts(): Observable<MutableList<AccountDTO>>

    @GET("/accounts/getAccountById")
    @Headers("Content-Type: application/json")
    fun getAccountById(@Query("id") id: Long): Observable<AccountDTO>

    @POST("/accounts/deposit")
    @Headers("Content-Type: application/json")
    fun makeDeposit(@Query("id") id: Long, @Query("amount") amount: BigDecimal): Observable<EmptyResult>

    @POST("/accounts/makeTransaction")
    @Headers("Content-Type: application/json")
    fun makeTransaction(
        @Query("fromAccountId") fromId: Long, @Query("toAccountId") toAccountId: Long, @Query(
            "amount"
        ) amount: BigDecimal, @Query("comment") comment: String
    ): Observable<EmptyResult>

    @GET("/accounts/getAllTransactions")
    @Headers("Content-Type: application/json")
    fun getAllTransactions(): Observable<MutableList<TransactionDTO>>

    @POST("/accounts/create")
    @Headers("Content-Type: application/json")
    fun createAccount(@Query("name") name: String): Observable<AccountDTO>

    @GET("/user/getInfo")
    @Headers("Content-Type: application/json")
    fun getProfile(): Observable<UserDTO>

    @POST("/accounts/close")
    @Headers("Content-Type: application/json")
    fun closeAccount(@Query("id") id: Long): Observable<AccountDTO>

    @Multipart
    @POST("/user/uploadImage")
    fun uploadImage(@Part file: MultipartBody.Part?): Observable<EmptyResult>

    @GET("/user/getImage")
    fun getImage(): Observable<ResponseBody>


}