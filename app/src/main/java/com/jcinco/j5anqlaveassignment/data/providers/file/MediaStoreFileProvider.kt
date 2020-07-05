package com.jcinco.j5anqlaveassignment.data.providers.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.jcinco.j5anqlaveassignment.data.model.file.FileInfo
import java.io.File

class MediaStoreFileProvider(val context: Context): IFileProvider {

    override val ROOT_FOLDER: String
        get() = "/"

    override fun getFiles(path: String): ArrayList<FileInfo>? {
        var fileInfoList = ArrayList<FileInfo>()
        if (path.equals(ROOT_FOLDER)) {
            fileInfoList.add(FileInfo("Images", "Images", null, true, null))
            fileInfoList.add(FileInfo("Audio", "Audio", null, true, null))
            fileInfoList.add(FileInfo("Video", "Video", null, true, null))
            fileInfoList.add(FileInfo("Files", "Files", null, true, null))
        }
        else if (path.toLowerCase().equals("images")) {
            fileInfoList = getImages()
        }
        else if (path.toLowerCase().equals("video")) {
            fileInfoList = getVideo()
        }

        return fileInfoList
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


    override fun isAvailable(): Boolean {
        return true
    }

    override fun getStorages(): ArrayList<FileInfo>? {
        return null
    }


    private fun getFromMediaStore(projection: Array<String>, contentUri: Uri, columnName: String): ArrayList<FileInfo> {
        var result: ArrayList<FileInfo> = ArrayList<FileInfo>()
        try {
            val cursor = context.contentResolver.query(contentUri, projection, null, null, null)
            if (cursor != null && cursor?.count > 0) {
                while (cursor.moveToNext()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(columnName)
                    val path = cursor.getString(columnIndex)
                    val fileInfo = FileInfo(null, null, null, false, File(path))
                    result.add(fileInfo)
                }
            }
        }
        catch (e:Exception) {
            e.printStackTrace()
        }
        return result
    }


}