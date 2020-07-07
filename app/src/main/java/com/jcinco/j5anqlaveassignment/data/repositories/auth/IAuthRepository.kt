package com.jcinco.j5anqlaveassignment.data.repositories.auth

import android.content.Intent

interface IAuthRepository {
    fun authenticate(username:String, password:String, callback: (success:Boolean)->Unit?)
    fun invalidate(username:String)
    fun handleAuthResponse(intent: Intent?)
}