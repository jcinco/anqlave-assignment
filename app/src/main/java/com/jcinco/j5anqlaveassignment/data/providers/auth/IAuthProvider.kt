package com.jcinco.j5anqlaveassignment.data.providers.auth

interface IAuthProvider {
    fun authenticate(username: String, password: String, callback: (success: Boolean)->Unit?)
    fun invalidate(username: String, callback: (success: Boolean) -> Unit?)
    fun isAuthenticated():Boolean
}