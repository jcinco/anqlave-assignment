package com.jcinco.j5anqlaveassignment.viewmodels.browser

import androidx.lifecycle.MethodCallsLogger
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcinco.j5anqlaveassignment.GlobalKeys
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import com.jcinco.j5anqlaveassignment.data.providers.file.GDriveFileProvider
import com.jcinco.j5anqlaveassignment.data.repositories.auth.IAuthRepository
import com.jcinco.j5anqlaveassignment.data.repositories.file.IFileRepository
import com.jcinco.j5anqlaveassignment.data.services.sec.FileEncryptionService
import com.jcinco.j5anqlaveassignment.data.services.sec.KeyStoreService
import com.jcinco.j5anqlaveassignment.utils.Coroutines
import com.jcinco.j5anqlaveassignment.utils.SharedPrefUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
    lateinit var authRepo: IAuthRepository
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
    fun openDir(fileInfo: FileInfo, isHttp: Boolean = false) {
        // if current dir is not null, push it into the back stack
        this.dirStack.push(currentDir)
        this.updateCurrent(fileInfo)
        getFiles(currentDir, isHttp)
    }

    /**
     * Gets the files fromt a directory file
     */
    fun getFiles(fileInfo: FileInfo? = null, isHttp: Boolean = false) {
        // if the file info is not a directory, do not proceed.
        if (fileInfo?.isDir == false)
            return

        this.isBusy.value = true
        if (!isHttp) {
            // Stop any running coroutine job
            this.stopActiveJob()

            // initialize by fetching the root folder list of files
            this.job = Coroutines.runInBackground(
                { fileRepo.getFiles(fileInfo) },
                {
                    files.value = it
                    this.isBusy.value = false
                })
        }
        else {

            fileRepo.getFiles(fileInfo) {list ->
                if (true) {
                    files?.value = list
                    this.isBusy.value = false
                }

            }
        }
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
                    encryptor.encrypAES(this.selectedFile?.file!!, derivedPasswd)
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
                    encryptor.decryptAES(this.selectedFile?.file!!, derivedPasswd)
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

        }
    }

    fun signOut(callback: (Boolean)->Unit?) {
        if (this.authRepo != null)
            CoroutineScope(Dispatchers.IO).launch {
                authRepo?.invalidate("") {
                    callback(it)
                }
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