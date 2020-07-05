package com.jcinco.j5anqlaveassignment.data.services.sec

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64

import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import java.lang.Exception
import javax.crypto.spec.IvParameterSpec


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
    private val CIPHER_INSTANCE: String = "AES/CBC/NoPadding"
    private val KEYSTORE_NAME = "AndroidKeyStore"


    public fun isAuthAliasExists(): Boolean {
        val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_NAME)
        if (keyStore != null) {
            try {
                keyStore.load(null)
                return keyStore.containsAlias(KEYSTORE_ALIAS)
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    public fun aliasExists(alias: String) : Boolean {
        val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_NAME)
        if (keyStore != null) {
            try {
                keyStore.load(null)
                return keyStore.containsAlias(alias)
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }


    /**
     * Encrypts the data and stores the secret into Android's KeyStore
     */
    public fun encrypt(alias: String, data: String): Pair<String, String> {
        try {
            var key: SecretKey = secretKeyForAlias(alias)
            val cipher = Cipher.getInstance(CIPHER_INSTANCE)
            cipher.init(Cipher.ENCRYPT_MODE, key)

            // Pad some spaces if the length is not a multiple of 16 to avoid
            // invalid block size exception.
            var dataTemp = data
            while(dataTemp.length % 16 != 0)
                dataTemp += "\u0020"

            val enc = cipher.doFinal(dataTemp.toByteArray(Charsets.UTF_8))

            // Return as pair of Base64 encoded strings for storage
            return Pair(
                Base64.encodeToString(cipher.iv, Base64.DEFAULT),
                Base64.encodeToString(enc, Base64.DEFAULT)
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair("", "")
    }



    public fun decrypt(alias: String, data: String, iv: String): String {
        var key: SecretKey = secretKeyForAlias(alias)
        val cipher = Cipher.getInstance(CIPHER_INSTANCE)

        // Decode from Base64 String
        val ivBytes = Base64.decode(iv, Base64.DEFAULT)
        val dataTmp = Base64.decode(data, Base64.DEFAULT)
        val ivSpec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)

        return String(cipher.doFinal(dataTmp)).trim()

    }



    public fun secretKeyForAlias(alias: String): SecretKey {
        val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_NAME)
        keyStore.load(null)

        if (!keyStore.containsAlias(alias)) {
            val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                KEYSTORE_NAME)
            val spec = getSpec(alias)
            keyGen.init(spec)
            return keyGen.generateKey()
        }
        else {

            val keyEntry = keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry
            return keyEntry.secretKey
        }
    }




    private fun getSpec(alias: String):KeyGenParameterSpec {
        return KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
    }

}