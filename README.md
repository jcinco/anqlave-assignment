# anqlave-assignment
I have adopted the MVVM / Repository pattern using Android's architecture components. This provides loose coupling, and allowing each component to be unit testable.
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


### US4 [Backlog] - As a user, I should be able to browse my GDrive files.
