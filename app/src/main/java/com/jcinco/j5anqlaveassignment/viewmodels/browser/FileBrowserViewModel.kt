package com.jcinco.j5anqlaveassignment.viewmodels.browser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import com.jcinco.j5anqlaveassignment.data.repositories.file.FileRepository
import com.jcinco.j5anqlaveassignment.data.repositories.file.IFileRepository
import com.jcinco.j5anqlaveassignment.utils.Coroutines
import kotlinx.coroutines.Job
import java.util.*
import kotlin.collections.ArrayList

class FileBrowserViewModel: ViewModel() {
    companion object {
        const val TAG = "FileBrowserViewModel"
    }
    // Coroutine job reference
    private lateinit var job: Job

    lateinit var fileRepo: IFileRepository
    var files = MutableLiveData<ArrayList<FileInfo>>()
    var currentDir: FileInfo? = null
    var dirStack: Stack<FileInfo> = Stack<FileInfo>()

    override fun onCleared() {
        super.onCleared()
         // kill the process
    }


    fun getFiles(fileInfo: FileInfo? = null) {
        // Stop any running coroutine job
        this.stopActiveJob()



        // initialize by fetching the root folder list of files
        this.job = Coroutines.runInBackground({
                if (fileInfo == null)
                    fileRepo.getFilesAtRoot(FileRepository.MODE_LOCAL)
                else
                    fileRepo.getFiles(fileInfo)
            },
            {
                files.value = it

                // if current dir is not null, push it into the back stack
                if (currentDir != null)
                    this.dirStack.push(currentDir)

                currentDir = fileInfo
            })
    }


    fun goBack() {
        if (!this.dirStack.empty()) {
            this.currentDir = this.dirStack.pop()
            this.getFiles(this.currentDir)
        }
    }


    private fun stopActiveJob() {
        if (::job.isInitialized) job.cancel()
    }

}