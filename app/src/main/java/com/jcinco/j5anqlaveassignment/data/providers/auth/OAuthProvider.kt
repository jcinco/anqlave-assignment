package com.jcinco.j5anqlaveassignment.data.providers.auth

import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.gson.GsonBuilder
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.utils.SharedPrefUtil
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.net.URL

class OAuthProvider(val context: Context): IAuthProvider {
    private val ACCESS_TOKEN = "ACCESS_TOKEN"
    private val OAUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth"

    private val kClientId = "client_id"
    private val kScope = "scope"
    private val kRedirectUri = "redirect_uri"
    private val kResponseType = "response_type"

    override fun authenticate(
        username: String,
        password: String,
        callback: (success: Boolean) -> Unit?
    ) {
        if (!hasAccessToken()) {
            // Request
            request()
        }
    }


    override fun invalidate(username: String, callback: (success: Boolean) -> Unit?) {
        TODO("Not yet implemented")
    }

    override fun isAuthenticated(): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAuthResponse(intent: Intent?) {
        if (intent?.data != null) {

        }
    }


    private fun getAccessToken() {
        Retrofit.Builder()
    }


    private fun request() {
        // Do request
        val config = getOAuthConfig()
        val url:String = "$OAUTH_URL?$kClientId=${config.client_id}&$kRedirectUri=${config.redirect_uri}&$kScope=${config.scope}&$kResponseType=code"
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.parse(url)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun getOAuthConfig():OAuthConfig {
        val json = context.resources.openRawResource(R.raw.authinfo)
            .bufferedReader().use { it.readText() }
        val config = GsonBuilder().create().fromJson(json, OAuthConfig::class.java)
        return config;
    }

    private fun hasAccessToken():Boolean {
        val sharedPref = SharedPrefUtil.getInstance()
        return sharedPref.get(ACCESS_TOKEN) != null
    }


    // Internal classes and interfaces

    internal data class OAuthConfig(
        val client_id: String,
        val api_key: String,
        val redirect_uri: String,
        val scope: String
    )

    // Retrofit related interfaces
    interface OAuth {
        @FormUrlEncoded
        @POST("oauth2/v4/token")
        fun requestToken(): Call<OAuthToken>
    }

    class OAuthToken {

    }


}