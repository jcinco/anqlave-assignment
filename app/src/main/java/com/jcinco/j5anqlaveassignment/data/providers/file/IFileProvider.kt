package com.jcinco.j5anqlaveassignment.data.providers.file

import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo


interface IFileProvider {
    val ROOT_FOLDER: String
        get() = "ROOT_FOLDER"


    /**
     * Returns the list of files in the directory. If the path is not a directory, null is returned.
     *
     * @param String - the directory path
     * @return ArrayList<FileInfo> - list of file info in that directory
     */
    fun getFiles(path: String): ArrayList<FileInfo>?
    fun getFilesAsync(path: String, callback: (ArrayList<FileInfo>)->Unit?)

    /**
     * Checks if the path is a directory
     *
     * @param String - path to check
     * @return Boolean - true if the path is a directory, false when otherwise.
     */
    fun isDir(path: String): Boolean

    fun isAvailable():Boolean
    fun getStorages(): ArrayList<FileInfo>?
}