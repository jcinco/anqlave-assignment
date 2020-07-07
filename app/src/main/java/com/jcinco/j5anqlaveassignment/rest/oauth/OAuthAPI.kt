package com.jcinco.j5anqlaveassignment.rest.oauth

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

// Retrofit related interfaces
interface OAuthAPI {
    @FormUrlEncoded
    @POST("oauth2/v4/token")
    fun requestToken(
        @Field("client_id") client_id:String,
        @Field("redirect_uri") redirect_uri:String,
        @Field("code") code: String,
        @Field("grant_type") grant_type:String
    ): Call<OAuthToken>
}