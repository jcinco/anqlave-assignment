package com.jcinco.j5anqlaveassignment.views.grdrive

import android.os.Bundle
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

    private fun setupDataBinding() {
        val authRepo = AuthRepository.getInstance()
        authRepo.authService = OAuthProvider(applicationContext)
        this.viewModel.authRepository = authRepo

        // setup data binding here
        val binding=
            DataBindingUtil.setContentView<ActivityGDriveBinding>(this, R.layout.activity_g_drive)
        binding.setLifecycleOwner(this)
        binding.viewModel = this.viewModel

        // observe for value
        this.viewModel.hasAccess.observe(this, Observer {
            if (it == false) {
                // Request OAuth for token

            }

        })
    }
}