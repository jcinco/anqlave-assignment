package com.jcinco.j5anqlaveassignment.views

import android.content.Intent
import android.os.Bundle
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.views.login.LoginActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onResume() {
        super.onResume()
        if (this.checkPermissions()) {
            this.showLogin()
        }
        else {
            this.requestPermissions()
        }
    }

    private fun showLogin() {
        // show login screen
        val intent = Intent(this.applicationContext, LoginActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        showLogin()
    }
}