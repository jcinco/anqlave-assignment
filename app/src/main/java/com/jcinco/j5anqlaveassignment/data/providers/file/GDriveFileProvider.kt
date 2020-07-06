package com.jcinco.j5anqlaveassignment.data.providers.file

import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo

class GDriveFileProvider: IFileProvider {
    override fun getFiles(path: String): ArrayList<FileInfo>? {
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