package com.jcinco.j5anqlaveassignment.data.providers.file

import android.content.Context
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo

abstract class FileProvider(val context: Context): IFileProvider {
    override fun getFiles(path: String): ArrayList<FileInfo>? {
        TODO("Not yet implemented")
    }

    override fun getFilesAsync(path: String, callback: (ArrayList<FileInfo>) -> Unit?) {
        TODO("Not yet implemented")
    }

    override fun isDir(path: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAvailable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getStorages(): ArrayList<FileInfo>? {
        TODO("Not yet implemented")
    }
}