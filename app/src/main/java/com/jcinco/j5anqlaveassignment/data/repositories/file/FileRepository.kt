package com.jcinco.j5anqlaveassignment.data.repositories.file

import android.os.Environment
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import com.jcinco.j5anqlaveassignment.data.providers.file.IFileProvider
import java.io.File


class FileRepository private constructor(): IFileRepository {
    companion object {
        const val TAG = "FileReqpository"
        const val MODE_LOCAL = "LOCAL"
        const val MODE_REMOTE = "REMOTE"

        @Volatile private var _instance: FileRepository? = null
        public fun getInstance(): FileRepository = _instance ?: synchronized(this) {
            _instance ?: FileRepository().also { _instance = it }
        }
    }

    lateinit var localFileProvider: IFileProvider
    lateinit var remoteFileProvider: IFileProvider
    var currentDir: FileInfo? = null

    private var mode:String = MODE_LOCAL


    override fun getFilesAtRoot(mode: String) : ArrayList<FileInfo> {
        // set the mode
        this.mode = mode;

        if (mode.equals(MODE_LOCAL)) {
            val root = Environment.getExternalStorageDirectory().absolutePath + "/"
            this.currentDir = FileInfo(null, null, null, false, File(root))
            return getFiles(this.currentDir!!)
        }
        else if (mode.equals(MODE_REMOTE)) {
            // connect to remote server here
        }
        return ArrayList<FileInfo>()
    }



    override fun getFiles(fileInfo: FileInfo) : ArrayList<FileInfo> {
        if (this.mode == MODE_LOCAL) {
            this.currentDir = fileInfo
            return localFileProvider.getFiles("${fileInfo.path}/") ?: ArrayList<FileInfo>()
        }
        return ArrayList<FileInfo>()
    }


}