package com.jcinco.j5anqlaveassignment.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jcinco.j5anqlaveassignment.viewmodels.browser.FileBrowserViewModel
import com.jcinco.j5anqlaveassignment.viewmodels.gdrive.GDriveViewModel
import com.jcinco.j5anqlaveassignment.viewmodels.login.LoginViewModel

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        }
        else if (modelClass.isAssignableFrom(FileBrowserViewModel::class.java)) {
            return FileBrowserViewModel() as T
        }
        else if (modelClass.isAssignableFrom(GDriveViewModel::class.java)) {
            return GDriveViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}