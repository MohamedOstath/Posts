package com.firstcode.posts.api

import com.google.gson.Gson
 import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object ServiceGenerator {
    private   val BASE_URL = "https://jsonplaceholder.typicode.com"
    private   val cacheSize = 5 * 1024 * 1024 // 5 MiB
        .toLong()


//    var OFFLINE_INTERCEPTOR = Interceptor { chain: Interceptor.Chain ->
//        var request = chain.request()
//        if (!AppUtil.isNetworkAvailable()) {
//            val maxStale = 60 * 60 * 24 * 7 // tolerate 4-weeks stale
//            request = request.newBuilder()
//                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
//                .build()
//        }
//        chain.proceed(request)
//    }
//    var ONLINE_INTERCEPTOR = Interceptor { chain: Interceptor.Chain ->
//        val response = chain.proceed(chain.request())
//        val maxAge = 30 // read from cache
//        response.newBuilder()
//            .header("Cache-Control", "public, max-age=$maxAge")
//            .build()
//    }

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
//            .addInterceptor(OFFLINE_INTERCEPTOR)
//            .addNetworkInterceptor(ONLINE_INTERCEPTOR)
//            .cache(cache())
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            /*.addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val builder = chain.request().newBuilder()
                builder.addHeader("Accept", "application/json")
                builder.addHeader(
                    "Accept-Language",
                    LocaleManager.getLanguage(MyApplication.getInstance())
                )
                val login: Login.AppUserBean = AppUtil.IsUserLogin()
                if (login != null) {
                    builder.addHeader("Authorization", "Bearer " + login.getAccess_token())
                }
                val request = builder.build()
                chain.proceed(request)
            })*/.build()
    }

    val client: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

}