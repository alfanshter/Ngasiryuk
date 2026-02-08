package com.example.ngasiryuk.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageUtils {

    /**
     * Save image from URI to internal storage
     * Returns the absolute path of saved file
     */
    fun saveImageToInternalStorage(context: Context, uri: Uri, fileName: String): String? {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Create directory for logo images
            val directory = File(context.filesDir, "logos")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // Create file with timestamp to avoid conflicts
            val timestamp = System.currentTimeMillis()
            val file = File(directory, "${fileName}_$timestamp.jpg")

            // Compress and save
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Delete old image file
     */
    fun deleteImageFile(filePath: String?): Boolean {
        if (filePath.isNullOrEmpty()) return false

        try {
            val file = File(filePath)
            if (file.exists()) {
                return file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * Get file from path
     */
    fun getImageFile(filePath: String?): File? {
        if (filePath.isNullOrEmpty()) return null

        val file = File(filePath)
        return if (file.exists()) file else null
    }
}

