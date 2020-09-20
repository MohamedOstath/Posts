package com.firstcode.posts.utils

import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import com.firstcode.posts.base.BaseApplication

object AppUtils {


    fun isNetworkAvailable(): Boolean {
        return try {
            val connectivityManager = BaseApplication.instance.getSystemService(
                AppCompatActivity.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


}