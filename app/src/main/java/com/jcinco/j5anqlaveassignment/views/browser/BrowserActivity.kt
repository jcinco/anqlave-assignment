package com.jcinco.j5anqlaveassignment.views.browser

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import com.jcinco.j5anqlaveassignment.data.providers.auth.LocalAuthProvider
import com.jcinco.j5anqlaveassignment.data.providers.auth.OAuthProvider
import com.jcinco.j5anqlaveassignment.data.providers.file.GDriveFileProvider
import com.jcinco.j5anqlaveassignment.data.providers.file.IFileProvider
import com.jcinco.j5anqlaveassignment.data.providers.file.LocalFileProvider
import com.jcinco.j5anqlaveassignment.data.providers.file.MediaStoreFileProvider
import com.jcinco.j5anqlaveassignment.data.repositories.auth.AuthRepository
import com.jcinco.j5anqlaveassignment.data.repositories.file.FileRepository
import com.jcinco.j5anqlaveassignment.databinding.ActivityFileBrowserBinding
import com.jcinco.j5anqlaveassignment.viewmodels.ViewModelFactory
import com.jcinco.j5anqlaveassignment.viewmodels.browser.FileBrowserViewModel
import com.jcinco.j5anqlaveassignment.views.BaseActivity
import com.jcinco.j5anqlaveassignment.views.browser.list.FileAdapter
import info.androidhive.fontawesome.FontDrawable
import kotlinx.android.synthetic.main.activity_file_browser.*
import kotlinx.android.synthetic.main.files_fragment.*
import java.io.File
import java.security.AuthProvider


class BrowserActivity: BaseActivity() {

    // view model reference
    protected val viewModel: FileBrowserViewModel
            by lazy {
                ViewModelProvider(this, ViewModelFactory).get(FileBrowserViewModel::class.java)
            }

    private var isContextSelection: Boolean = false
    private lateinit var fileProvider: IFileProvider


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

        // Recycler view optimization attempts :D
        this.recyclerView.setHasFixedSize(true)
        this.recyclerView.setItemViewCacheSize(20)
        this.recyclerView.isDrawingCacheEnabled = true
        this.recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH


        // Add the toolbar
        val bar = this.toolbar
        setSupportActionBar(bar)


        val isGDrive = intent.getBooleanExtra("GDRIVE", false)

        if (isGDrive) {
            fileProvider = GDriveFileProvider(applicationContext)

        }
        else

            // set the provider to MediaStoreFileProvider for versions greater than P
            // Environment.getExternalStorageDir() has been deprecated in SDK 29
            fileProvider = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
                    MediaStoreFileProvider(applicationContext)
                else
                    LocalFileProvider(applicationContext)

        // Setup the repo

        val fileRepo = FileRepository.getInstance()
        fileRepo.localFileProvider = fileProvider
        this.viewModel.fileRepo = fileRepo

        // Initialize the root directory
        val rootFileInfo = FileInfo(fileProvider.ROOT_FOLDER,
            fileProvider.ROOT_FOLDER, null, true, null)

        // if the list of files is empty on initialization,
        // then load the root
        if (this.viewModel.files?.value == null)
            this.viewModel.openDir(rootFileInfo, isGDrive)

        // Bind the progress indicator with the isBusy state
        this.viewModel.isBusy.observe(this, Observer {
            if (it) {
                this.progressBar.visibility = View.VISIBLE
            }
            else {
                this.progressBar.visibility = View.GONE
            }
        })

        // listen for updates to the list of files and update the list on changes
        this.viewModel?.files?.observe(this, Observer { files ->
            recyclerView.also {
                it.layoutManager = LinearLayoutManager(applicationContext)
                (it.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
                it.setHasFixedSize(true)

                it.adapter = FileAdapter(files) { it, action ->
                    if (this.viewModel.isBusy.value == false) {
                        if (it.isDir == true)
                            this.viewModel?.openDir(it)
                        else if (action == 1) {
                            this.viewModel.selectedFile = it
                            isContextSelection = true
                        }
                    }
                }
            }
        })
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val signOutIcon = FontDrawable(applicationContext,
            R.string.fa_sign_out_alt_solid, true, false)
        signOutIcon.textSize = 30f
        signOutIcon.clearColorFilter()
        signOutIcon.setTextColor(R.color.white)


        menuInflater.inflate(R.menu.browser_menu, menu)
        val item = menu?.getItem(0)
        item?.icon = signOutIcon

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.viewModel.signOut()
        this.finish()
        return true
    }



    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (isContextSelection && !this.viewModel?.isBusy?.value!!) {
           if (item?.itemId == 200) { // encrypt
               this.viewModel.encrypt()
           }
            else { // decrypt
               this.viewModel.decrypt()
           }
            // unset the flag
            isContextSelection = false
        }
        return super.onContextItemSelected(item)
    }

    /**
     * Trap the system nav back button press and use it to
     * navigate up a folder in our browser.
     */
    override fun onBackPressed() {
        this.viewModel.goBack()
    }
}