package com.jcinco.j5anqlaveassignment

import android.app.Application
import com.jcinco.j5anqlaveassignment.data.services.sec.KeyStoreService

class AssignmentApp: Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize keychain service wrapper here
        // we need the context to access the keystore
        KeyStoreService.getInstance().context = applicationContext

    }


}