package com.jcinco.j5anqlaveassignment.rest

import android.content.Context
import com.jcinco.j5anqlaveassignment.rest.oauth.OAuthInterceptor
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File

class RetrofitFactory(val context: Context) {
    companion object {
        @Volatile private var _instance: RetrofitFactory? = null
        fun getInstance(context: Context):RetrofitFactory = _instance ?: synchronized(this) {
            _instance ?: RetrofitFactory(context).also{ _instance = it}
        }
    }

    /**
     * Creates an instance of Retrofit for a given URL
     *
     * @param String - the base url
     * @return Retrofit - Instance of retrofit associated to the url
     */
    fun createClient(baseUrl: String, httpClient: OkHttpClient): Retrofit {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl).client(httpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }


    fun getOAuthHttpClient(interceptor: Interceptor): OkHttpClient {
        val dir = File(context.cacheDir, "jikorsty")
        val cacheDir = Cache(dir, 1024*1024)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .cache(cacheDir)
            .build()
    }

}