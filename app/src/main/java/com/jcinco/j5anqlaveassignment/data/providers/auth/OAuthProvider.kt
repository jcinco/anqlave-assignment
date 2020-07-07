package com.jcinco.j5anqlaveassignment.data.providers.auth

import android.content.Context
import com.jcinco.j5anqlaveassignment.utils.SharedPrefUtil

class OAuthProvider(val context: Context): IAuthProvider {
    private val ACCESS_TOKEN = "ACCESS_TOKEN"

    override fun authenticate(
        username: String,
        password: String,
        callback: (success: Boolean) -> Unit?
    ) {
        if (!hasAccessToken()) {
            // Request

        }
    }

    override fun invalidate(username: String, callback: (success: Boolean) -> Unit?) {
        TODO("Not yet implemented")
    }

    override fun isAuthenticated(): Boolean {
        TODO("Not yet implemented")
    }



    private fun hasAccessToken():Boolean {
        val sharedPref = SharedPrefUtil.getInstance()
        return sharedPref.get(ACCESS_TOKEN) != null
    }

}