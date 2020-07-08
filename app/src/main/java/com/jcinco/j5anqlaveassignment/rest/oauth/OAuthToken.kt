package com.jcinco.j5anqlaveassignment.rest.oauth

data class OAuthToken(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val refresh_token: String
)