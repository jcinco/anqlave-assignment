package com.jcinco.j5anqlaveassignment.data.providers.file

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import java.io.File
import java.nio.file.*

class LocalFileProvider: IFileProvider{
    companion object {
        var context: Context? = null
    }

    override val ROOT_FOLDER: String
        get() = Environment.getExternalStorageDirectory().absolutePath


    /**
     * Returns the list of files in the directory. If the path is not a directory, null is returned.
     *
     * @param String - the directory path
     * @return ArrayList<FileInfo> - list of file info in that directory
     */
    override fun getFiles(path: String): ArrayList<FileInfo>? {
        val dirPath = path

        if (this.isDir(dirPath)) {
            var infoList: ArrayList<FileInfo> = ArrayList<FileInfo>()
            var fileList = File("$dirPath/").listFiles()
            if (fileList != null) {
                fileList.forEach { f ->
                    val isDir = this.isDir(f.absolutePath)
                    val info = FileInfo(null, null, null, isDir, file = f)
                    infoList.add(info)
                }
            }
            return infoList
        }
        return null
    }

    override fun isAvailable(): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true
        }
        return false
    }

    override fun getStorages(): ArrayList<FileInfo>? {
        var list = ArrayList<FileInfo>()
        val file = Environment.getExternalStorageDirectory()
        Log.d("path", file?.absolutePath)
        val storages = file?.listFiles()
        if (storages != null) {
            storages?.forEach {
                list.add(FileInfo(null, null, null, false, it))
            }
        }
        return list
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