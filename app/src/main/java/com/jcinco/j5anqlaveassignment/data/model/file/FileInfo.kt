package com.jcinco.j5anqlaveassignment.data.model.file

import java.io.File
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
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
                val fileBytes = getMarker()
                if(fileBytes != null && fileBytes?.size > 0) {
                    val strMarker = String(fileBytes).trim()
                    isEncrypted = strMarker.equals("enc")
                }
            }

        }
    }

    private fun getMarker(): ByteArray {
        if (file != null) {
            val reader = InputStreamReader(file?.inputStream())
            var c: CharArray = CharArray(3)
            val charSet: Charset = Charset.forName("UTF-8")

            reader.read(c, 0, 3)

            val byteBuffer: ByteBuffer = charSet.encode(CharBuffer.wrap(c))

            return Arrays.copyOf(byteBuffer.array(), byteBuffer.limit())
        }
        return ByteArray(0)
    }
}
