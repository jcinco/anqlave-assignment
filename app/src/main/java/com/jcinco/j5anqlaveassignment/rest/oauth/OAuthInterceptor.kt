package com.jcinco.j5anqlaveassignment.rest.oauth

import okhttp3.Interceptor
import okhttp3.Response

class OAuthInterceptor(
    val accessToken: String,
    val tokenType: String
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val origRequest = chain.request()
        val request = origRequest.newBuilder()
            .header("Authorization", "$tokenType $accessToken")
            .build()
        return chain.proceed(request)
    }
}