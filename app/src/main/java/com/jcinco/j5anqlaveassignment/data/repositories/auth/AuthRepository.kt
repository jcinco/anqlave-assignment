package com.jcinco.j5anqlaveassignment.data.repositories.auth

import android.content.Intent
import com.jcinco.j5anqlaveassignment.data.providers.auth.IAuthProvider
import com.jcinco.j5anqlaveassignment.data.providers.auth.LocalAuthProvider

// The single source of truth
class AuthRepository private constructor(): IAuthRepository {

    companion object {
        private const val TAG = "AuthRepository"

        // Singleton implementation
        @Volatile private var _instance: AuthRepository? = null
        public fun getInstance() = _instance ?: synchronized(this) {
            _instance ?: AuthRepository().also { _instance = it }
        }
    }

    //
    lateinit var authService: IAuthProvider
    lateinit var remoteAuthService: IAuthProvider


    /**
     * Auhenticates the user
     *
     * @param String - the username
     * @param String - password
     * @param callback - callback function called after authentication routine has completed.
     */
    override fun authenticate(username:String, password:String, callback: (success:Boolean)->Unit?) {
        // First check for local info on credentials.
        if (authService != null) {
            authService?.authenticate(username, password, callback)
        }
        else if (remoteAuthService != null) {
            remoteAuthService?.authenticate(username, password, callback)
        }
        else {
            // user does not exist
            callback(false)
        }

    }

    override fun invalidate(username: String, callback: (success: Boolean) -> Unit?) {
        authService.invalidate(username, callback)
    }

    override fun handleAuthResponse(intent: Intent?) {
        authService.handleAuthResponse(intent)
    }

    override fun getAuthorizationHeader(): String? {
        return authService.getAuthorizationHeader()
    }


}