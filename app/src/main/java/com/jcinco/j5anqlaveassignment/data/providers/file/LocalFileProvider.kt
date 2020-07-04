package com.jcinco.j5anqlaveassignment.data.providers.file

import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import java.io.File
import java.nio.file.*

class LocalFileProvider: IFileProvider{

    /**
     * Returns the list of files in the directory. If the path is not a directory, null is returned.
     *
     * @param String - the directory path
     * @return ArrayList<FileInfo> - list of file info in that directory
     */
    override fun getFiles(path: String): ArrayList<FileInfo>? {
        if (this.isDir(path)) {
            var infoList: ArrayList<FileInfo> = ArrayList<FileInfo>()
            var fileList = File(path).listFiles()
            fileList.forEach { f ->
                val isDir = this.isDir(f.absolutePath)
                val info = FileInfo(null, null, null, isDir, file = f)
                infoList.add(info)
            }
            return infoList
        }
        return null
    }

    /**
     * Checks if the path is a directory
     *
     * @param String - path to check
     * @return Boolean - true if the path is a directory, false when otherwise.
     */
    fun isDir(path: String) : Boolean {
        val pathObj = File(path).toPath()
        return Files.exists(pathObj) && pathObj != null
    }

}