package com.jcinco.j5anqlaveassignment.views

import android.content.Intent
import android.os.Bundle
import com.jcinco.j5anqlaveassignment.views.grdrive.GDriveActivity
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.views.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.localBtn.setOnClickListener {
            this.showLogin()
        }

        this.gdriveBtn.setOnClickListener {
            this.showGDrive()
        }
    }


    override fun onResume() {
        super.onResume()

    }

    private fun showLogin() {
        // show login screen
        val intent = Intent(this.applicationContext, LoginActivity::class.java)
        this.startActivity(intent)
        //this.finish()
    }

    private fun showGDrive() {
        val intent = Intent(this.applicationContext, GDriveActivity::class.java)
        this.startActivity(intent)
        //this.finish()
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