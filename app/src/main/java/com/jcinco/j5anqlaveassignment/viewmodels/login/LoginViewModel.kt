package com.jcinco.j5anqlaveassignment.viewmodels.login

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcinco.j5anqlaveassignment.data.repositories.auth.AuthRepository

public class LoginViewModel : ViewModel() {
    // static declarations here
    companion object {
        const val TAG: String = "LoginViewModel"
    }

    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var errorVisibility = MutableLiveData<Int>()

    init {
        this.errorVisibility.value = View.GONE
    }

    public fun login() {
        if (this.username.value.isNullOrEmpty() || this.password.value.isNullOrEmpty()) {
            // handle empty inputs
            this.errorVisibility.value = View.VISIBLE
            return
        }
        val username = this.username.value
        val password = this.password.value
        AuthRepository.getInstance().authenticate(username ?: "", password ?: "") { success:Boolean ->
            if (success)
                this.errorVisibility.value = View.GONE
            else
                this.errorVisibility.value = View.VISIBLE

        }


    }
}