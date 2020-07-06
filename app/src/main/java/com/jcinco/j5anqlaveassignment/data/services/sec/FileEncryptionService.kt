package com.jcinco.j5anqlaveassignment.data.services.sec

import android.util.Base64
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class FileEncryptionService {
    companion object {
        const val TAG = "FileEncryptionService"

        @Volatile private var _instance: FileEncryptionService? = null
        fun getInstance(): FileEncryptionService = _instance ?: synchronized(this) {
            _instance ?: FileEncryptionService().also { _instance = it }
        }
    }

    private val ALGORITHM = "AES/GCM/NoPadding"
    private val SALT_SIZE:Int = 16 // 16 bytes
    private val ITERATION_COUNT: Int = 100000 // iteration count 100,000
    private val PBKDF2_HASH_FUNC_NAME: String = "PBKDF2WithHmacSHA256"
    private val IV_SIZE:Int = 12 // 12 bytes
    private val TAG_SIZE:Int = 16
    private val MARKER: String = "enc"

    /**
     * Encrypts a file:
     * Using AES encryption
     *      256-bit key
     *      Mode - GCM
     *      IV -12 bytes
     *      Tag Size -16
     *      Encryption key (256-bit key) should be derived from the predefined password using PBKDF2
     * PBKDF2 parameters:
     *      Salt - 16 bytes
     *      Iteration - 100000
     *      Hashing Function - SHA-256
     *
     * @param FileInfo - the file to encrypt
     * @param String - the password
     * @return Pair<ByteArray,ByteArray> - first - initialization vector, second - encrypted contents
     */
    fun encrypAES(file: File, password: String): ByteArray {
        val salt = ByteArray(SALT_SIZE)
        val random = SecureRandom()

        // get random salt
        random.nextBytes(salt)

        // get file contents as byte array
        val fileContents = file.readBytes()
        // key should be 256 bits
        val pwSpec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, 256)
        val key = SecretKeyFactory.getInstance(PBKDF2_HASH_FUNC_NAME)
            .generateSecret(pwSpec).encoded

        // init vector of 12 bytes
        val iv = ByteArray(IV_SIZE)
        random.nextBytes(iv)

        val cipher = Cipher.getInstance(ALGORITHM)
        val secretKeySpec = SecretKeySpec(key, "AES")
        val gcmSpec = GCMParameterSpec(TAG_SIZE, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmSpec)

        val enc = cipher.doFinal(fileContents)
        val marker = MARKER.toByteArray(Charsets.UTF_8)

        //
        // concat marker + salt + iv and place it in the file's header
        //
        val finalEnc = ByteArray(marker.size + SALT_SIZE + IV_SIZE + enc.size)

        System.arraycopy(marker, 0, finalEnc, 0, marker.size)
        System.arraycopy(salt, 0, finalEnc, marker.size, SALT_SIZE)
        System.arraycopy(iv, 0, finalEnc,  marker.size + SALT_SIZE, IV_SIZE)
        System.arraycopy(enc, 0, finalEnc, marker.size + SALT_SIZE + IV_SIZE, enc.size)

        return finalEnc
    }



    /**
     * Decrypts the file that was encrypted using encryptAES().
     *
     * @param FileInfo - the file to decrypt
     * @param String - the password
     */
    fun decryptAES(file:File, password:String): ByteArray {
        val fileContent = file.readBytes()

        // Extract the salt and init vector from the byte array
        val salt = ByteArray(SALT_SIZE)
        val iv = ByteArray(IV_SIZE)
        val marker = ByteArray(MARKER.length)
        val enc = ByteArray(fileContent.size - (SALT_SIZE + IV_SIZE + marker.size))

        System.arraycopy(fileContent, 0, marker, 0, marker.size)
        System.arraycopy(fileContent, marker.size, salt, 0, SALT_SIZE)
        System.arraycopy(fileContent, marker.size + SALT_SIZE, iv, 0, IV_SIZE)
        System.arraycopy(fileContent, marker.size + SALT_SIZE + IV_SIZE, enc, 0,fileContent.size - (SALT_SIZE + IV_SIZE + marker.size))

        // key should be 256 bits
        val pwSpec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, 256)
        val key = SecretKeyFactory.getInstance(PBKDF2_HASH_FUNC_NAME)
            .generateSecret(pwSpec).encoded

        val cipher = Cipher.getInstance(ALGORITHM)
        val secretKeySpec = SecretKeySpec(key, "AES")
        val gcmSpec = GCMParameterSpec(TAG_SIZE, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmSpec)

        val final = cipher.doFinal(enc)
        return final
    }


    /**
     * Returns a SHA-256 hash of a string.
     *
     * @param String - the input string to hash
     * @return String - the hashed version of the input string
     */
    fun hash(input:String) : String {
        val md = MessageDigest.getInstance("SHA-256")
        try {
            md.update(input.toByteArray(Charsets.UTF_8))
            val digest = md.digest()
            val final = Base64.encodeToString(digest, Base64.DEFAULT)
            return final
        }
        catch(e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

}