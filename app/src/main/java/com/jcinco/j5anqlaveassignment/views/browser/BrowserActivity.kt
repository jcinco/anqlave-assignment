package com.jcinco.j5anqlaveassignment.views.browser

import android.os.Bundle
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.data.providers.file.IFileProvider
import com.jcinco.j5anqlaveassignment.data.providers.file.LocalFileProvider
import com.jcinco.j5anqlaveassignment.data.repositories.file.FileRepository
import com.jcinco.j5anqlaveassignment.databinding.ActivityFileBrowserBinding
import com.jcinco.j5anqlaveassignment.viewmodels.ViewModelFactory
import com.jcinco.j5anqlaveassignment.viewmodels.browser.FileBrowserViewModel
import com.jcinco.j5anqlaveassignment.views.BaseActivity


class BrowserActivity: BaseActivity() {

    // view model reference
    protected val viewModel: FileBrowserViewModel
            by lazy {
                ViewModelProvider(this, ViewModelFactory).get(FileBrowserViewModel::class.java)
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setupDataBinding()
    }

    override fun onResume() {
        super.onResume()
        // Recheck permissions
        if (!this.checkPermissions()) {
            this.requestPermissions()
        }

    }

    private fun setupDataBinding() {
        // setup data binding here
        val binding=
            DataBindingUtil.setContentView<ActivityFileBrowserBinding>(this, R.layout.activity_file_browser)
        binding.setLifecycleOwner(this)
        binding.viewModel = this.viewModel

        // Setup the repo
        val fileProvider = LocalFileProvider()
        val fileRepo = FileRepository.getInstance()
        fileRepo.localFileProvider = fileProvider

        this.viewModel.fileRepo = fileRepo

    }


    override fun onBackPressed() {
        this.viewModel.goBack()
    }
}