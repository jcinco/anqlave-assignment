package com.jcinco.j5anqlaveassignment.viewmodels.gdrive

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcinco.j5anqlaveassignment.data.repositories.auth.AuthRepository
import com.jcinco.j5anqlaveassignment.utils.SharedPrefUtil

class GDriveViewModel: ViewModel() {
    private val ACCESS_TOKEN = "ACCESS_TOKEN"

    var authRepository: AuthRepository? = null
    set(value) {
        field = value
    }
    get() = field



    // public
    var hasAccess = MutableLiveData<Boolean>()

    init {
        this.checkForAccess()
    }

    fun checkForAccess() {
        this.hasAccess.value = this.hasAccessToken()
    }

    fun requestAuth() {
        // run OAuth request
        this.authRepository?.authenticate("",""){}
    }

    fun handleOAuthResponse(intent: Intent?) {
        this.authRepository?.handleAuthResponse(intent)
    }

    private fun hasAccessToken():Boolean {
        val sharedPref = SharedPrefUtil.getInstance()
        return sharedPref.get(ACCESS_TOKEN) != null
    }
}