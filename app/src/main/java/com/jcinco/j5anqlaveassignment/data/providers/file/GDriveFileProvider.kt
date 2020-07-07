package com.jcinco.j5anqlaveassignment.data.providers.file

import android.content.ComponentCallbacks
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import com.jcinco.j5anqlaveassignment.data.model.file.GDFile
import com.jcinco.j5anqlaveassignment.data.providers.auth.OAuthProvider
import com.jcinco.j5anqlaveassignment.rest.RetrofitFactory
import com.jcinco.j5anqlaveassignment.rest.gdrive.GDGetFiles
import com.jcinco.j5anqlaveassignment.rest.oauth.OAuthInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GDriveFileProvider(context: Context): FileProvider(context) {
    override val ROOT_FOLDER: String
        get() = "HOME"

    private val BASE_URL = "https://www.googleapis.com/"
    private var oAuthProvider: OAuthProvider

    init {
        this.oAuthProvider = OAuthProvider(context)
    }


    override fun getFiles(path: String, callback: (ArrayList<FileInfo>)->Unit?) {
        var files = ArrayList<FileInfo>()
        val factory = RetrofitFactory.getInstance(context)

        val interceptor = OAuthInterceptor(
            oAuthProvider.getAccessToken(),
            oAuthProvider.getTokenType())

        val retrofit = factory.createClient(BASE_URL, factory.getOAuthHttpClient(interceptor))
        val getFiles = retrofit.create(GDGetFiles::class.java)
        val call = getFiles.files()
        val listener = object: Callback<Any?> {
            override fun onResponse(
                call: Call<Any?>,
                response: Response<Any?>
            ) {
                callback(ArrayList<FileInfo>())
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        }
        call.enqueue(listener)
    }


    override fun isDir(path: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAvailable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getStorages(): ArrayList<FileInfo>? {
        TODO("Not yet implemented")
    }
}