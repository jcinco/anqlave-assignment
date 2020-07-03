package com.jcinco.j5anqlaveassignment.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jcinco.j5anqlaveassignment.databinding.ActivityLoginBinding

import androidx.lifecycle.ViewModelProvider
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.viewmodels.ViewModelFactory
import com.jcinco.j5anqlaveassignment.viewmodels.login.LoginViewModel

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
        // setup data binding here
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.setLifecycleOwner(this)
        binding.viewModel = this.viewModel
    }

}