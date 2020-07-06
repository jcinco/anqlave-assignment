# anqlave-assignment
I have adopted the MVVM / Repository pattern using Android's architecture components. This provides loose coupling, and allowing each component to be unit testable.
This design pattern essentially separates the business logic from the view. The common mistake is bloating the activity / view controller (in iOS) with business logic and data, blurring the MV* pattern.

## High level design
![img](https://github.com/jcinco/anqlave-assignment/blob/master/uml/app_structure.png)

## Functional Requirements

### US1 - As a user, I should be able to login to the application using a predefined username and password.
1. Login using defined credentials.
- username: admin
- password: p@s5W0rd

2. The app should store the credentials in a secure manner.
- The credentials are hashed using SHA-256 algorithm (username and password) before persistently stored in app storage. 
- The secret key is stored in Android's KeyStore for encrypting / decrypting the default password used to derive PBKDF2 secret key in encrypting and decrypting files. The decryptable password is stored separately.


#### Login screen class diagram
![img](https://github.com/jcinco/anqlave-assignment/blob/master/uml/userlogin_class_diagram.png)

#### Login screen sequence diagram
![img](https://github.com/jcinco/anqlave-assignment/blob/master/uml/user_login_sequence.png)

### US2 - As a user, I should be able to browse my local files.
1. Check for READ and WRITE permissions to external storage permissions.
- If permission was not granted, request persmission at runtime.
2. Check for the Android version. 
- If the version level is below 29, we utilize LocalFileProvider which employs direct access to external storage using Environment.
- If the version level is >= 29, we use the MediaStoreFileProvider which uses the media store content provider. This is a change in Android 10, deprecating the Environment.getExternalStorageDirectory(), which no longer works in higher versions of Android starting 29.
3. Initialize by loading files from the defined root folder in the provider.
4. Ensure navigation in and out of folders.
- Employed a back stack which keeps track of the parent folders.
- Utilized the system back button to go back up a folder when tapped. 
#### File browser class diagram
![img](https://github.com/jcinco/anqlave-assignment/blob/master/uml/file_browser_class.png)
#### File browser sequence diagram
![img](https://github.com/jcinco/anqlave-assignment/blob/master/uml/file_browser_seq.png)
### US3 - As a user, I should be able to encrypt or decrypt a file.
Requirements:
- Using AES encryption
  - 256-bit key
  - Mode - GCM
  - IV -12 bytes
  - Tag Size -16
- Encryption key (256-bit key) should be derived from the predefined password using PBKDF2
  - PBKDF2 parameters:
  - Salt - 16 bytes
  - Iteration - 100000
  - Hashing Function - SHA-256
  
```kotlin
class FileEncryptionService {
    ...
    private val ALGORITHM = "AES/GCM/NoPadding" // Mode - GCM
    private val SALT_SIZE:Int = 16 // 16 bytes
    private val ITERATION_COUNT: Int = 100000 // iteration count 100,000
    private val PBKDF2_HASH_FUNC_NAME: String = "PBKDF2WithHmacSHA256" // 256-bit PBKDF2
    private val IV_SIZE:Int = 12 // 12 bytes initialization vector
    private val TAG_SIZE:Int = 16 // Tag size - 16
    private val MARKER: String = "enc"

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
    ...
}
```

### US4 [Backlog] - As a user, I should be able to browse my GDrive files.
