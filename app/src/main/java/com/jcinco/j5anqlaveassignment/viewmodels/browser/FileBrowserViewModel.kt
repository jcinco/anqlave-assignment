package com.jcinco.j5anqlaveassignment.viewmodels.browser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcinco.j5anqlaveassignment.GlobalKeys
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import com.jcinco.j5anqlaveassignment.data.repositories.file.IFileRepository
import com.jcinco.j5anqlaveassignment.data.services.sec.FileEncryptionService
import com.jcinco.j5anqlaveassignment.data.services.sec.KeyStoreService
import com.jcinco.j5anqlaveassignment.utils.Coroutines
import com.jcinco.j5anqlaveassignment.utils.SharedPrefUtil
import kotlinx.coroutines.Job
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class FileBrowserViewModel: ViewModel() {
    companion object {
        const val TAG = "FileBrowserViewModel"
    }
    // Coroutine job reference
    private lateinit var job: Job
    private lateinit var encJob: Job

    lateinit var fileRepo: IFileRepository
    var files = MutableLiveData<ArrayList<FileInfo>>()
    var currentDirectory = MutableLiveData<String>()
    var currentDir: FileInfo? = null
    var dirStack: Stack<FileInfo> = Stack<FileInfo>()
    var selectedFile: FileInfo? = null
    var isBusy = MutableLiveData<Boolean>()


    init {
        this.isBusy.value = false
    }


    override fun onCleared() {
        super.onCleared()

        // kill any live process
        stopActiveJob()
    }


    /**
     * Gets the files list from the directory and pushes any current dir info into the stack.
     *
     * @param FileInfo - the file info of the directory
     */
    fun openDir(fileInfo: FileInfo) {
        // if current dir is not null, push it into the back stack
        this.dirStack.push(currentDir)
        this.updateCurrent(fileInfo)
        getFiles(currentDir)
    }

    /**
     * Gets the files fromt a directory file
     */
    fun getFiles(fileInfo: FileInfo? = null) {
        // if the file info is not a directory, do not proceed.
        if (fileInfo?.isDir == false)
            return

        // Stop any running coroutine job
        this.stopActiveJob()
        this.isBusy.value = true
        this.files.value = ArrayList<FileInfo>() // empty list

        // initialize by fetching the root folder list of files
        this.job = Coroutines.runInBackground(
            { fileRepo.getFiles(fileInfo) },
            {
                files.value = it
                this.isBusy.value = false
            })
    }

    /**
     * Go back to previous directory.
     *
     */
    fun goBack() {
        if (this.dirStack.size > 1) {
            this.updateCurrent(this.dirStack.pop())
            this.getFiles(this.currentDir)
        }
    }


    /**
     * Encrypts the file selected
     */
    fun encrypt() {
        if (this.selectedFile != null
            && this.selectedFile?.isEncrypted != true) {

            this.stopActiveJob()
            this.isBusy.value = true
            encJob = Coroutines.runInBackground(
                {
                    val encryptor = FileEncryptionService.getInstance()
                    val derivedPasswd = getPassword()

                    // get encoded bytes
                    encryptor.encrypAES(this.selectedFile!!, derivedPasswd)
                },
                {
                    // Save file here
                    val file = File(this.selectedFile?.path)
                    val out = file.outputStream()
                    out.write(it)
                    out.close()
                    //this.getFiles(this.currentDir)
                    this.selectedFile?.parseFile()
                    this.files.value = this.files.value?.clone() as ArrayList<FileInfo>

                    // unselect the file
                    this.selectedFile = null
                    this.isBusy.value = false
                })


//            val encryptor = FileEncryptionService.getInstance()
//            val derivedPasswd = getPassword()
//
//            // get encoded bytes
//            val encBytes = encryptor.encrypAES(this.selectedFile!!, derivedPasswd)
//
//            // Save file here
//            val file = File(this.selectedFile?.path)
//            val out = file.outputStream()
//            out.write(encBytes)
//            out.close()
//            this.getFiles(this.currentDir)
//
//            // unselect the file
//            this.selectedFile = null
        }
    }

    /**
     * Decryots the file selected
     */
    fun decrypt() {
        if (this.selectedFile != null
            && this.selectedFile?.isEncrypted == true) {

            this.stopActiveJob()
            this.isBusy.value = true

            encJob = Coroutines.runInBackground(
                {
                    val encryptor = FileEncryptionService.getInstance()
                    val derivedPasswd = getPassword()

                    // get encoded bytes
                    encryptor.decryptAES(this.selectedFile!!, derivedPasswd)
                },
                {
                    // Save file here
                    val file = File(this.selectedFile?.path)
                    val out = file.outputStream()
                    out.write(it)
                    out.close()
                    this.selectedFile?.parseFile()
                    this.files.value = this.files.value?.clone() as ArrayList<FileInfo>

                    // unselect the file
                    this.selectedFile = null
                    this.isBusy.value = false
                })


//            val encryptor = FileEncryptionService.getInstance()
//            val derivedPasswd = getPassword()
//
//            // get decoded bytes
//            val encBytes = encryptor.decryptAES(this.selectedFile!!, derivedPasswd)
//
//            // Save file here
//            val file = File(this.selectedFile?.path)
//            val out = file.outputStream()
//            out.write(encBytes)
//            out.close()
//            this.getFiles(this.currentDir)
//
//            // unselect the file
//            this.selectedFile = null
        }
    }


    private fun getPassword(): String {
        val sharedPref = SharedPrefUtil.getInstance()
        val encPW: String? = sharedPref.get(GlobalKeys.ENC_PW)
        val iv: String? = sharedPref.get(GlobalKeys.IV)
        return KeyStoreService.getInstance()
            .decrypt(GlobalKeys.KEYSTORE_ALIAS,
            encPW ?: "",
            iv ?: "")
    }


    private fun updateCurrent(fileInfo: FileInfo) {
        currentDir = fileInfo
        currentDirectory.value = currentDir?.path
    }

    private fun stopActiveJob() {
        if (::job.isInitialized) job.cancel()
        if (::encJob.isInitialized) encJob.cancel()
    }

}