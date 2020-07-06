# anqlave-assignment
I have adopted the MVVM / Repository pattern using Android's architecture components. This provides lose coupling, and allowing each components to be unit testable.
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


#### - Login screen class diagram
![img](https://github.com/jcinco/anqlave-assignment/blob/master/uml/userlogin_class_diagram.png)

#### - Login screen sequence diagram
![img](https://github.com/jcinco/anqlave-assignment/blob/master/uml/user_login_sequence.png)

### US2 - As a user, I should be able to browse my local files.
### US3 - As a user, I should be able to encrypt or decrypt a file.

### US4 [Backlog] - As a user, I should be able to browse my GDrive files.
