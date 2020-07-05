package com.jcinco.j5anqlaveassignment.data.model.file

import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


data class FileInfo(
    var name:String?,
    var path:String?,
    var dateModified: String?,
    var isDir: Boolean? = false,
    var file: File?) {

    var isEncrypted:Boolean  = false

    constructor(): this(null, null, null, false, null)

    init {
        parseFile()
    }

    fun parseFile() {
        if (file != null) {
            this.path = file?.absolutePath
            this.isDir = (file?.listFiles() != null)
            this.name = file?.name

            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            this.dateModified = dateFormat.format(file?.lastModified())

            if (this.isDir != true) {
                // read first 3 bytes of the file
                val marker = ByteArray(3)
                val fileBytes = file?.readBytes()

                if(fileBytes != null && fileBytes?.size > 3) {
                    System.arraycopy(fileBytes, 0, marker, 0, 3)
                    val strMarker = String(marker).trim()
                    isEncrypted = strMarker.equals("enc")
                }
            }

        }
    }
}
