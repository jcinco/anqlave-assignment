package com.jcinco.j5anqlaveassignment.data.providers.auth

class OAuthProvider: IAuthProvider {
    override fun authenticate(
        username: String,
        password: String,
        callback: (success: Boolean) -> Unit?
    ) {
        TODO("Not yet implemented")
    }

    override fun invalidate(username: String, callback: (success: Boolean) -> Unit?) {
        TODO("Not yet implemented")
    }
}