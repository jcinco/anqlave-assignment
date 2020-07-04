package com.jcinco.j5anqlaveassignment.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


object Coroutines {
    fun<T: Any> runInBackground(task: suspend(()-> T?), result: ((T?)->Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async mark@{
                return@mark task()
            }.await()
            result(data)
        }
}