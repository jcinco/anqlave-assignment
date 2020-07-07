package com.jcinco.j5anqlaveassignment.data.providers.auth

import com.jcinco.j5anqlaveassignment.GlobalKeys
import com.jcinco.j5anqlaveassignment.data.services.sec.FileEncryptionService
import com.jcinco.j5anqlaveassignment.data.services.sec.KeyStoreService
import com.jcinco.j5anqlaveassignment.utils.SharedPrefUtil

public class LocalAuthProvider: IAuthProvider {
    // Static members here
    companion object {
        // Singleton implementation
        @Volatile private var _instance: LocalAuthProvider? = null

        fun getInstance() = _instance ?: synchronized(this) {
                _instance ?: LocalAuthProvider().also { _instance = it }
            }
    }

    val keyStoreService = KeyStoreService.getInstance()
    private var isAuth = false

    init {
        // setup the default credentials on initialization
        this.setUpDefaultAuthCredentials()
    }

    private fun setUpDefaultAuthCredentials() {
        val ksService = KeyStoreService.getInstance()

        // Ensure that the predefined credentials are only created once.
        if (!ksService.aliasExists(GlobalKeys.KEYSTORE_ALIAS)) {
            val pref = SharedPrefUtil.getInstance()
            val encSvc = FileEncryptionService.getInstance()
            val defaultUser = "admin"
            val defaultPassword = "p@s5W0rd"

            // keep the hash of the username and password
            val hashedUsername = encSvc.hash(defaultUser).trim()
            val hashedPassword = encSvc.hash(defaultPassword).trim()
            // save an decryptable password to use for key derivation
            val enc = ksService.encrypt(GlobalKeys.KEYSTORE_ALIAS, defaultPassword)

            // save credentials in shared preferences
            pref.save(GlobalKeys.USERNAME, hashedUsername)
            pref.save(GlobalKeys.PASSWORD, hashedPassword)
            pref.save(GlobalKeys.IV, enc.first)
            pref.save(GlobalKeys.ENC_PW, enc.second)
        }
    }

    fun hasKey() : Boolean {
       return keyStoreService.isAuthAliasExists()
    }

    override fun authenticate(username: String, password: String, callback: (success: Boolean)->Unit?) {
        // retrieve the init vector and the encrypted password from shared preference
        val localPassword = SharedPrefUtil.getInstance().get(GlobalKeys.PASSWORD)
        val localUsername = SharedPrefUtil.getInstance().get(GlobalKeys.USERNAME)
        if (localPassword != null && localUsername != null) {
            val hashedUsername = FileEncryptionService.getInstance().hash(username).trim()
            val hashedPassword = FileEncryptionService.getInstance().hash(password).trim()

            // pass the comparison result to the callback. if the encrypted password matches
            // with the supplied password, then the user is allowed to proceed.
            this.isAuth = localPassword.equals(hashedPassword) && localUsername.equals(hashedUsername)
        }
        else {
            this.isAuth = false
        }
        callback(this.isAuth)
    }

    override fun invalidate(username: String, callback: (success: Boolean) -> Unit?) {
        this.isAuth = false
        callback(false)
    }

    override fun isAuthenticated(): Boolean {
        return this.isAuth
    }
}