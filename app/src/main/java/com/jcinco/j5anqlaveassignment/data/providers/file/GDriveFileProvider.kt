package com.jcinco.j5anqlaveassignment.data.providers.file

import android.content.Context
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import com.jcinco.j5anqlaveassignment.data.model.file.GDFile
import com.jcinco.j5anqlaveassignment.rest.RetrofitFactory
import com.jcinco.j5anqlaveassignment.rest.gdrive.GDGetFiles
import com.jcinco.j5anqlaveassignment.rest.oauth.OAuthInterceptor
import retrofit2.Call

class GDriveFileProvider(context: Context): FileProvider(context) {
    override val ROOT_FOLDER: String
        get() = "HOME"

    private val BASE_URL = "https://www.googleapis.com/"

    private fun fetchFilesFromServer(): ArrayList<FileInfo> {
        var files = ArrayList<FileInfo>()
        val retrofit = RetrofitFactory.getInstance(context)
        //val interceptor = OAuthInterceptor()
        //val getFiles = retrofit.createClient()
        return files
    }

    override fun getFiles(path: String): ArrayList<FileInfo>? {
        if (path.equals(ROOT_FOLDER)) {
            return fetchFilesFromServer()
        }
        return null
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