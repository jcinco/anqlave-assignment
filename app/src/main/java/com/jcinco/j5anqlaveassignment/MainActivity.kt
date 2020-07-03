package com.jcinco.j5anqlaveassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jcinco.j5anqlaveassignment.views.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // show login screen
        val intent = Intent(this.applicationContext, LoginActivity::class.java)
        this.startActivity(intent)
        this.finish()

    }
}