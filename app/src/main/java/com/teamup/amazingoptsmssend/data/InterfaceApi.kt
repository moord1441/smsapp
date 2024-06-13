package com.teamup.amazingoptsmssend.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @GET("exec")
    fun sendData(@Query("id") id: String): Call<Void>
}
