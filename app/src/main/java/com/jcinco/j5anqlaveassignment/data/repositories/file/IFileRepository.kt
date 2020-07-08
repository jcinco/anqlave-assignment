package com.jcinco.j5anqlaveassignment.data.repositories.file

import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo

interface IFileRepository {
    fun currentDirectory(): FileInfo?
    fun getFilesAtRoot(mode: String) : ArrayList<FileInfo>
    fun getFiles(fileInfo: FileInfo?): ArrayList<FileInfo>
    fun getFiles(fileInfo: FileInfo?, callback: (ArrayList<FileInfo>)->Unit?)
}