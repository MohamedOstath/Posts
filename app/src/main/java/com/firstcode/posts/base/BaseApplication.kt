package com.firstcode.posts.base

import android.app.Application

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
         instance = this


    }

    companion object {
        lateinit var instance: BaseApplication
            private set
    }


}