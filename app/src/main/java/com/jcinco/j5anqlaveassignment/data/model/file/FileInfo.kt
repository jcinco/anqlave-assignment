package com.jcinco.j5anqlaveassignment.data.model.file

import java.io.File

data class FileInfo(
    var name:String?,
    var path:String?,
    var dateModified: String?,
    var isDir: Boolean? = false,
    var file: File?) {

    init {
        if (file != null) {
            this.name = if (isDir == true) file?.name + "/" else file?.name
            this.path = file?.absolutePath
        }
    }
}
