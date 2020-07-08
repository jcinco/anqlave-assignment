package com.jcinco.j5anqlaveassignment.views.grdrive

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.data.providers.auth.OAuthProvider
import com.jcinco.j5anqlaveassignment.data.repositories.auth.AuthRepository
import com.jcinco.j5anqlaveassignment.databinding.ActivityFileBrowserBinding
import com.jcinco.j5anqlaveassignment.databinding.ActivityGDriveBinding
import com.jcinco.j5anqlaveassignment.viewmodels.ViewModelFactory
import com.jcinco.j5anqlaveassignment.viewmodels.browser.FileBrowserViewModel
import com.jcinco.j5anqlaveassignment.viewmodels.gdrive.GDriveViewModel
import com.jcinco.j5anqlaveassignment.views.browser.BrowserActivity

class GDriveActivity : AppCompatActivity() {

    // view model reference
    protected val viewModel: GDriveViewModel
            by lazy {
                ViewModelProvider(this, ViewModelFactory).get(GDriveViewModel::class.java)
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()

    }

    override fun onResume() {
        super.onResume()

        this.viewModel.requestAuthCallback = {
            this.onAuthRequestResult(it)
        }
        // if data is not null, we assume that a redirect from google launched this activity
        if (intent.data != null) {
            this.viewModel.handleOAuthResponse(intent)
        }
        else
            this.viewModel.requestAuth(){}
    }

    private fun onAuthRequestResult(success: Boolean) {
        if (success) {
            val i = Intent(applicationContext, BrowserActivity::class.java)
            i.putExtra("GDRIVE", true)
            startActivity(i)
            finish()
        }
        else {
            Toast.makeText(applicationContext, "Failed to get permission to access GDrive", Toast.LENGTH_LONG).show()
            finish()
        }
    }


    private fun setupDataBinding() {
        val authRepo = AuthRepository.getInstance()
        authRepo.authService = OAuthProvider(applicationContext)
        this.viewModel.authRepository = authRepo

        // setup data binding here
        val binding=
            DataBindingUtil.setContentView<ActivityGDriveBinding>(this, R.layout.activity_g_drive)
        binding.setLifecycleOwner(this)
        binding.viewModel = this.viewModel


    }
}