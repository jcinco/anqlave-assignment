package com.jcinco.j5anqlaveassignment.viewmodels.login

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcinco.j5anqlaveassignment.data.repositories.auth.IAuthRepository

public class LoginViewModel : ViewModel() {
    // static declarations here
    companion object {
        const val TAG: String = "LoginViewModel"
    }

    lateinit var authRepository:IAuthRepository

    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var errorVisibility = MutableLiveData<Int>()
    var isAuthenticated = MutableLiveData<Boolean>()

    init {
        this.errorVisibility.value = View.GONE
        // Debug
        this.username.value = "admin"
        this.password.value = "p@s5W0rd"

        this.isAuthenticated.value = false
    }

    public fun login() {
        if (this.username.value.isNullOrEmpty() || this.password.value.isNullOrEmpty()
            || this.authRepository == null) {
            // handle empty inputs
            this.errorVisibility.value = View.VISIBLE
            return
        }
        val username = this.username.value
        val password = this.password.value

        authRepository.authenticate(username ?: "", password ?: "") { success:Boolean ->
            this.isAuthenticated.value = success == true
            if (success)
                this.errorVisibility.value = View.GONE
            else
                this.errorVisibility.value = View.VISIBLE

        }
    }


}