package com.jcinco.j5anqlaveassignment.views.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jcinco.j5anqlaveassignment.databinding.ActivityLoginBinding

import androidx.lifecycle.ViewModelProvider
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.data.providers.auth.LocalAuthProvider
import com.jcinco.j5anqlaveassignment.data.repositories.auth.AuthRepository
import com.jcinco.j5anqlaveassignment.viewmodels.ViewModelFactory
import com.jcinco.j5anqlaveassignment.viewmodels.login.LoginViewModel
import com.jcinco.j5anqlaveassignment.views.browser.BrowserActivity

public class LoginActivity : AppCompatActivity() {

    // static declarations
    companion object {
        const val TAG: String = "LoginActivity"
    }

    // view model reference
    protected val viewModel: LoginViewModel
        by lazy {
            ViewModelProvider(this, ViewModelFactory).get(LoginViewModel::class.java)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setupDataBinding()
    }

    private fun setupDataBinding() {

        // inject the dependencies
        viewModel.authRepository = AuthRepository.getInstance()
        (viewModel.authRepository as AuthRepository).localAuthService = LocalAuthProvider.getInstance()



        // setup data binding here
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.setLifecycleOwner(this)
        binding.viewModel = this.viewModel

        // listen for a successful result on authentication
        this.viewModel.isAuthenticated.observe(this, Observer {
            if (it) {
                val intent = Intent(this, BrowserActivity::class.java)
                startActivity(intent)
            }
        })
    }

}