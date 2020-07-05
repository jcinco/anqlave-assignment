package com.jcinco.j5anqlaveassignment.views.browser.list

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jcinco.j5anqlaveassignment.R
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import com.jcinco.j5anqlaveassignment.data.providers.file.LocalFileProvider
import com.jcinco.j5anqlaveassignment.data.repositories.file.FileRepository
import com.jcinco.j5anqlaveassignment.viewmodels.ViewModelFactory
import com.jcinco.j5anqlaveassignment.viewmodels.browser.FileBrowserViewModel
import kotlinx.android.synthetic.main.files_fragment.*

class FilesFragment: Fragment() {
    // view model reference
    protected var viewModel: FileBrowserViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(FileBrowserViewModel::class.java)
        }

        return inflater.inflate(R.layout.files_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // setup view model
        val fileProvider = LocalFileProvider()
        val fileRepository = FileRepository.getInstance()
        val rootDir = Environment.getExternalStorageDirectory()
        val rootFileInfo = FileInfo(null, null, null, false, rootDir)

        this.viewModel?.fileRepo = fileRepository
        this.viewModel?.openDir(rootFileInfo)

        // listen for updates to the list of files
        this.viewModel?.files?.observe(viewLifecycleOwner, Observer { files ->
            filesRecyclerView.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                (it.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
                it.setHasFixedSize(true)
                it.adapter = FileAdapter(files, { it, action ->
                    if (it.isDir == true)
                        this.viewModel?.openDir(it)
                    // else
                })
            }
        })
    }
}