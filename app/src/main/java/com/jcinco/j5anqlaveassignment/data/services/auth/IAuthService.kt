package com.jcinco.j5anqlaveassignment.data.services.auth

interface IAuthService {
    fun authenticate(username: String, password: String, callback: (success: Boolean)->Unit?)
    fun invalidate(username: String, callback: (success: Boolean) -> Unit?)
}