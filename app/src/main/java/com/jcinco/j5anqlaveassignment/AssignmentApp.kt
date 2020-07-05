package com.jcinco.j5anqlaveassignment

import android.app.Application
import com.jcinco.j5anqlaveassignment.data.providers.file.LocalFileProvider
import com.jcinco.j5anqlaveassignment.data.services.sec.KeyStoreService
import com.jcinco.j5anqlaveassignment.utils.SharedPrefUtil
import java.security.KeyStore

class AssignmentApp: Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize the singleton classes that require the context on app creation.
        // We want to avoid passing the context from the activity.
        KeyStoreService.getInstance().context = applicationContext
        SharedPrefUtil.getInstance().context = applicationContext
    }




}