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
import com.jcinco.j5anqlaveassignment.GlobalKeys
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.data.services.sec.FileEncryptionService
import com.jcinco.j5anqlaveassignment.data.services.sec.KeyStoreService
import com.jcinco.j5anqlaveassignment.rest.RetrofitFactory
import com.jcinco.j5anqlaveassignment.rest.oauth.OAuthAPI
import com.jcinco.j5anqlaveassignment.rest.oauth.OAuthInterceptor
import com.jcinco.j5anqlaveassignment.rest.oauth.OAuthToken
import com.jcinco.j5anqlaveassignment.utils.SharedPrefUtil
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.net.URL

class OAuthProvider(val context: Context): IAuthProvider {
    private val ACCESS_TOKEN = "ACCESS_TOKEN"
    private val OAUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth"
    private val BASE_URL = "https://www.googleapis.com/"

    private val kClientId = "client_id"
    private val kScope = "scope"
    private val kRedirectUri = "redirect_uri"
    private val kResponseType = "response_type"
    private var oauthToken: OAuthToken? = null

    lateinit var requestorCallback: (success:Boolean)->Unit?


    override fun authenticate(
        username: String,
        password: String,
        callback: (success: Boolean) -> Unit?
    ) {
        if (!hasAccessToken()) {
            this.requestorCallback = callback
            // Request
            request()
        }
        else {
            callback(false)
        }
    }


    override fun invalidate(username: String, callback: (success: Boolean) -> Unit?) {
        TODO("Not yet implemented")
    }

    override fun isAuthenticated(): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleAuthResponse(intent: Intent?) {
        if (intent?.data != null &&
                intent?.data is Uri) {
            val uri = intent?.data
            val code = uri?.getQueryParameter("code") as String
            getAccessToken(code)
        }
    }


    private fun getAccessToken(code: String) {
        val factory = RetrofitFactory.getInstance(context)
        val interceptor = OAuthInterceptor("", "")
        val retrofit = factory.createClient(BASE_URL, factory.getOAuthHttpClient(interceptor))

        // get an instance of OAuthAPI (REST endpoint representation in retrofit)
        val oauthApi = retrofit.create(OAuthAPI::class.java)

        // fetch the oauth config from raw resource
        val config = getOAuthConfig()

        val call = oauthApi.requestToken(
            config.client_id,
            config.redirect_uri,
            code,
            "authorization_code")



        val callback = object: Callback<OAuthToken> {
            override fun onFailure(call: Call<OAuthToken>, t: Throwable) {
                requestorCallback(false)
            }

            override fun onResponse(call: Call<OAuthToken>, response: Response<OAuthToken>) {
                oauthToken = response.body()
                secureSaveTokens(oauthToken)
                // save the token and respond successful
                requestorCallback(true)
            }
        }
        call.enqueue(callback)
    }


    fun getAccessToken():String {
        if (hasAccessToken()) {
            val sharedPref = SharedPrefUtil.getInstance()
            val encToken = sharedPref.get(GlobalKeys.ACCESS_TOKEN)
            val iv = sharedPref.get(GlobalKeys.AT_IV)
            return KeyStoreService.getInstance().decrypt(KeyStoreService.KEYSTORE_ALIAS, encToken!!, iv!!)
        }
        return ""
    }


    private fun secureSaveTokens(oauthToken: OAuthToken?) {
        // Encrypt the tokens and save to shared pref
        val sharePref = SharedPrefUtil.getInstance()
        val keystore = KeyStoreService.getInstance()
        val alias = KeyStoreService.KEYSTORE_ALIAS
        val encAccessToken = keystore.encrypt(alias, oauthToken?.access_token!!)
        val encRefreshToken = keystore.encrypt(alias, oauthToken?.refresh_token!!)

        // save the tokens
        sharePref.save(GlobalKeys.ACCESS_TOKEN, encAccessToken.second)
        sharePref.save(GlobalKeys.AT_IV, encAccessToken.first)

        sharePref.save(GlobalKeys.REFRESH_TOKEN, encRefreshToken.second)
        sharePref.save(GlobalKeys.RT_IV, encRefreshToken.first)

    }

    private fun getPassword(): String {
        val sharedPref = SharedPrefUtil.getInstance()
        val encPW: String? = sharedPref.get(GlobalKeys.ENC_PW)
        val iv: String? = sharedPref.get(GlobalKeys.IV)
        return KeyStoreService.getInstance()
            .decrypt(
                GlobalKeys.KEYSTORE_ALIAS,
                encPW ?: "",
                iv ?: "")
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
        return sharedPref.get(GlobalKeys.ACCESS_TOKEN) != null
    }



    // Internal classes and interfaces

    internal data class OAuthConfig(
        val client_id: String,
        val api_key: String,
        val redirect_uri: String,
        val scope: String
    )




}