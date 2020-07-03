package com.jcinco.j5anqlaveassignment.data.services.sec

import android.content.Context
import android.content.SharedPreferences
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.security.auth.x500.X500Principal

public class KeyStoreService() {
    companion object {
        public const val TAG: String = "KeyStoreService"
        public const val KEYSTORE_ALIAS:String = "com.jcinco.default"

        @Volatile private var _instance: KeyStoreService? = null
        public fun getInstance() = _instance ?: synchronized(this) {
            _instance ?: KeyStoreService().also { _instance = it }
        }
    }

    // Keystore related handling
    private var _context:Context? = null
    public var context: Context?
    set(ctx) {
        _context = ctx
    }
    get() {
        return _context
    }


    private val KEYSTORE_ALIAS:String = "com.jcinco.default"
    private val CIPHER_INSTANCE: String = "AES/GCM/NoPadding"
    private val KEYSTORE_NAME = "AndroidKeyStore"
    private val KEY_IV = "KEY_IV"
    private var keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_NAME)


    public fun isAuthAliasExists(): Boolean {
        if (keyStore != null) {
           return keyStore.containsAlias(KEYSTORE_ALIAS)
        }
        return false
    }

    /**
     * Encrypts the data and stores the secret into Android's KeyStore
     */
    public fun encrypt(alias: String, data: String): String {
        // encrypt
        if (context == null)
            throw Exception("Context is not set")

        keyStore.load(null)

        // Attempt to get the secret from keystore
        val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE_NAME)

        // if the alias does not exist
        if (!keyStore.containsAlias(alias)) {
            val spec = getSpec(alias, KeyProperties.PURPOSE_ENCRYPT)
            keyGen.init(spec)
        }
        var key: SecretKey = keyStore.getKey(alias, null) as SecretKey? ?: keyGen.generateKey()
        val cipher = Cipher.getInstance(CIPHER_INSTANCE)
        cipher.init(Cipher.ENCRYPT_MODE, key)

        return String(cipher.doFinal(data.toByteArray(Charsets.UTF_8)))
    }



    public fun decrypt(alias: String, data: String): String {
        // encrypt
        if (context == null)
            throw Exception("Context is not set")

        keyStore.load(null)

        // Attempt to get the secret from keystore
        val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE_NAME)

        // if the alias does not exist
        if (!keyStore.containsAlias(alias)) {
            val spec = getSpec(alias, KeyProperties.PURPOSE_DECRYPT)
            keyGen.init(spec)
        }
        var key: SecretKey = keyStore.getKey(alias, null) as SecretKey? ?: keyGen.generateKey()
        val cipher = Cipher.getInstance(CIPHER_INSTANCE)
        cipher.init(Cipher.DECRYPT_MODE, key)

        return String(cipher.doFinal(data.toByteArray(Charsets.UTF_8)))
    }



    private fun getSpec(alias: String, purpose: Int):KeyGenParameterSpec {
        return KeyGenParameterSpec.Builder(alias, purpose)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
    }

}