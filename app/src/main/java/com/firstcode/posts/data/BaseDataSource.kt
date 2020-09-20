package com.firstcode.posts.data

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Results<T> {

        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Results.success(body)
                }
            }
//            Log.e( "Error_", " ${response.code()} ${response.errorBody()?.string()}")
            return error(response.code())
        } catch (e: Exception) {
            Log.e("Error_", " Error ===---------------------------" + e.message)
            return error(e.message ?: e.toString())
        }
    }
//
//    private fun <T> error(message: String): Results<T> {
//        return Results.error(null, "Network call has failed for a following reason: $message")
//    }
//
//    private fun <T> error(errorBody: ResponseBody?, code: Int): Results<T> {
//        return Results.error(errorBody, "$code")
//    }

}

