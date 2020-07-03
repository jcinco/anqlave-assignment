package com.jcinco.j5anqlaveassignment.data.repositories.auth

import com.jcinco.j5anqlaveassignment.data.services.auth.LocalAuthService

// The single source of truth
public class AuthRepository {

    companion object {
        private const val TAG = "AuthRepository"

        // Singleton implementation
        @Volatile private var _instance: AuthRepository? = null
        public fun getInstance() = _instance ?: synchronized(this) {
            _instance ?: AuthRepository().also { _instance = it }
        }
    }

    /**
     * Auhenticates the user
     *
     * @param String - the username
     * @param String - password
     * @param callback - callback function called after authentication routine has completed.
     */
    public fun authenticate(username:String, password:String, callback: (success:Boolean)->Unit?) {
        if (LocalAuthService.getInstance().hasKey()) {
            LocalAuthService.getInstance().authenticate(username, password, callback)
        }
    }


}