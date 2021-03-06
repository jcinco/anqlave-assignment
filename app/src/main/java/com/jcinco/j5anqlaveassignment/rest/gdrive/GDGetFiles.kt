package com.jcinco.j5anqlaveassignment.rest.gdrive

import com.jcinco.j5anqlaveassignment.data.model.file.GDFile
import com.jcinco.j5anqlaveassignment.rest.oauth.OAuthToken
import retrofit2.Call
import retrofit2.http.*

interface GDGetFiles {

    @GET("drive/v2/files")
    fun files(): Call<GDFile>

    @GET("drive/v2/files/{id}")
    fun file(@Path("id") id:Long): Call<GDFile>
}