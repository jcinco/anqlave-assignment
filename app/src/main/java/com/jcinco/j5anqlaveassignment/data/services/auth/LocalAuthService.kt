package com.jcinco.j5anqlaveassignment.data.services.auth

import com.jcinco.j5anqlaveassignment.data.services.sec.KeyStoreService

public class LocalAuthService: IAuthService {
    // Static members here
    companion object {
        // Singleton implementation
        @Volatile private var _instance: LocalAuthService? = null

        fun getInstance() =
            _instance ?: synchronized(this) {
                _instance ?: LocalAuthService().also { _instance = it }
            }
    }

    val keyStoreService = KeyStoreService.getInstance()

    fun hasKey() : Boolean {
       return keyStoreService.isAuthAliasExists()
    }

    override fun authenticate(username: String, password: String, callback: (success: Boolean)->Unit?) {
        val enc = keyStoreService.encrypt(KeyStoreService.KEYSTORE_ALIAS, password)
        val dec = keyStoreService.decrypt(KeyStoreService.KEYSTORE_ALIAS, enc)
        // if it exists, fetch password from keystore and check if password matches
        // else if it does not exist, return an error.
        callback(false)
    }

    override fun invalidate(username: String, callback: (success: Boolean) -> Unit?) {
        callback(false)
    }
}