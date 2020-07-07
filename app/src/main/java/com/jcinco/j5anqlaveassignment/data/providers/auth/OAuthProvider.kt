package com.jcinco.j5anqlaveassignment.data.providers.auth

import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
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


    private fun request() {
        val am = (context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager)
        val options = Bundle()
        val accountType = am.getAccounts().filter { it.type.equals("com.google") }
        val handler = Handler(context.mainLooper, Handler.Callback {
            false
        })
        am.getAuthToken(
            accountType.first(),
            "Access files",
            true,
            {},
            handler
        )

    }
    

    private fun hasAccessToken():Boolean {
        val sharedPref = SharedPrefUtil.getInstance()
        return sharedPref.get(ACCESS_TOKEN) != null
    }

}