package com.jcinco.j5anqlaveassignment.data.providers.file

import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo

class RemoteFileProvider: IFileProvider {
    override fun getFiles(path: String): ArrayList<FileInfo>? {
        return null
    }

    override fun getFiles(path: String, callback: (ArrayList<FileInfo>) -> Unit?) {
        TODO("Not yet implemented")
    }

    override fun isDir(path: String): Boolean {
        return false
    }

    override fun getStorages(): ArrayList<FileInfo>? {
        return null
    }

    override fun isAvailable(): Boolean {
        return false
    }
}