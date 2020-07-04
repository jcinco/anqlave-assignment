package com.jcinco.j5anqlaveassignment.data.providers.file

import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo


interface IFileProvider {
    fun getFiles(path: String): ArrayList<FileInfo>?
}