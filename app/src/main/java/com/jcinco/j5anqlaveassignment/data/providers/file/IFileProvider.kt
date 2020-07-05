package com.jcinco.j5anqlaveassignment.data.providers.file

import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo


interface IFileProvider {
    val ROOT_FOLDER: String
        get() = "ROOT_FOLDER"

    fun getFiles(path: String): ArrayList<FileInfo>?
    fun isAvailable():Boolean
    fun getStorages(): ArrayList<FileInfo>?
}