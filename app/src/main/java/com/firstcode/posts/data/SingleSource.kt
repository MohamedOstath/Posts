package com.firstcode.posts.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


fun <A> resultLiveData(networkCall: suspend () -> Results<A>): LiveData<Results<A?>> =
    liveData(Dispatchers.IO) {
        emit(Results.loading())
        delay(1500)
        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Results.Status.SUCCESS) {
            emit(Results.success(responseStatus.data))
        } else if (responseStatus.status == Results.Status.ERROR) {
            emit(Results.error(responseStatus.message!!))
        }
    }

fun <T> resultLiveData(databaseQuery: () -> LiveData<T>): LiveData<Results<T>> =
    liveData(Dispatchers.IO) {
        emit(Results.loading())
        val source = databaseQuery.invoke().map {
            Results.success(it)
        }
        emitSource(source)
    }
