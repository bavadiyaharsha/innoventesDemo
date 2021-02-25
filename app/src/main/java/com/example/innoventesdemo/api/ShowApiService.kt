package com.example.innoventesdemo.api

import android.provider.Settings
import com.example.innoventesdemo.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class ShowApiService {
    private var retrofit: Retrofit? = null

    /**
     * return retrofit instance
     *
     * @return
     */
    private fun initializeRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        if (retrofit == null) {
            val builder = OkHttpClient.Builder().readTimeout(110, TimeUnit.SECONDS)
                .connectTimeout(110, TimeUnit.SECONDS).writeTimeout(110, TimeUnit.SECONDS)
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request: Request = chain.request().newBuilder()
                        .addHeader("device-type", "android")
                        .build()
                    return chain.proceed(request)
                }
            })
            builder.addInterceptor(httpLoggingInterceptor)
            val okHttpClient = builder.build()
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.DATA_REQUEST_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    /**
     * return api reference
     * @return
     */
    val api: ShowApi
        get() = retrofit!!.create(ShowApi::class.java)

    companion object {
        @Volatile
        var instance: ShowApiService? = null
            get() {
                if (field == null) {
                    synchronized(ShowApiService::class.java) {
                        if (field == null) {
                            field = ShowApiService()
                        }
                    }
                }
                return field
            }
            private set
    }

    init {
        initializeRetrofit()
    }
}