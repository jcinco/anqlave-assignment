package com.jcinco.j5anqlaveassignment.data.providers.file

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import java.io.File

class MediaStoreFileProvider(val context: Context): IFileProvider {
    companion object {
        const val IMAGES: String = "Images"
        const val VIDEO: String = "Video"
        const val AUDIO: String = "Audio"
        const val FILES: String = "Documents"
    }


    override val ROOT_FOLDER: String
        get() = "/"


    override fun getFiles(path: String): ArrayList<FileInfo>? {
        var fileInfoList = ArrayList<FileInfo>()
        if (path.equals(ROOT_FOLDER)) {
            fileInfoList.add(FileInfo(IMAGES, IMAGES, null, true, null))
            fileInfoList.add(FileInfo(AUDIO, AUDIO, null, true, null))
            fileInfoList.add(FileInfo(VIDEO, VIDEO, null, true, null))
            fileInfoList.add(FileInfo(FILES, FILES, null, true, null))
        }
        else if (path.toLowerCase().equals("images")) {
            fileInfoList = getImages()
        }
        else if (path.toLowerCase().equals("video")) {
            fileInfoList = getVideo()
        }
        else if (path.toLowerCase().equals("audio")) {
            fileInfoList = getAudio()
        }
        else if (path.toLowerCase().equals(FILES.toLowerCase())) {
            fileInfoList = getDocuments()
        }

        return fileInfoList
    }





    override fun isDir(path: String): Boolean {
        return (path.equals(ROOT_FOLDER) ||
                path.equals(IMAGES) ||
                path.equals(VIDEO) ||
                path.equals(FILES) ||
                path.equals(AUDIO))
    }



    private fun getVideo(): ArrayList<FileInfo> {
        val proj = arrayOf(MediaStore.Video.Media.DATA)
        return this.getFromMediaStore(proj,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Video.Media.DATA)
    }


    private fun getAudio(): ArrayList<FileInfo> {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        return this.getFromMediaStore(proj,
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Audio.Media.DATA)
    }


    private fun getImages(): ArrayList<FileInfo> {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        return this.getFromMediaStore(proj,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Images.Media.DATA)
    }

    private fun getDocuments(): ArrayList<FileInfo> {
        val proj = arrayOf(MediaStore.Files.FileColumns.DATA)
        val mimeVar = "${MediaStore.Files.FileColumns.MIME_TYPE}=?"
        var selection = ""
        val mimes = arrayOf(
            "application/pdf",
            "application/msword",
            "application/msword",
            "application/rtf",
            "application/vnd.ms-excel",
            "application/vnd.ms-powerpoint",
            "application/zip",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.oasis.opendocument.text",
            "application/vnd.oasis.opendocument.spreadsheet",
            "application/vnd.oasis.opendocument.presentation",
            "application/json",
            "text/javascript",
            "application/octet-stream",
            "application/vnd.amazon.ebook",
            "application/x-tar",
            "text/html",
            "text/plain",
            "text/css",
            "text/xml",
            "text/csv"
        )


        mimes.forEach {
            if (selection.isNullOrEmpty())
                selection = mimeVar
            else
                selection = "$selection OR $mimeVar"
        }


        return this.getFromMediaStore(proj,
            MediaStore.Files.getContentUri("external"),
            MediaStore.Files.FileColumns.DATA,
            selection,
            mimes)
    }


    override fun isAvailable(): Boolean {
        return true
    }

    override fun getStorages(): ArrayList<FileInfo>? {
        return null
    }


    private fun getFromMediaStore(projection: Array<String>,
                                  contentUri: Uri,
                                  columnName: String,
                                  selection: String? = null,
                                  selectionArgs: Array<String>? = null): ArrayList<FileInfo> {
        var result: ArrayList<FileInfo> = ArrayList<FileInfo>()
        try {
            val cursor = context.contentResolver
                .query(contentUri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor?.count > 0) {
                while (cursor.moveToNext()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(columnName)
                    val path = cursor.getString(columnIndex)
                    var fileInfo = FileInfo()
                    try {
                        fileInfo = FileInfo(null, null, null, false, File(path))
                        result.add(fileInfo)
                    }
                    catch(e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }
        catch (e:Exception) {
            e.printStackTrace()
        }
        return result
    }


}