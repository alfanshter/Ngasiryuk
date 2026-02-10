package com.example.ngasiryuk.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.ngasiryuk.data.local.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DatabaseManager {
    private const val DATABASE_NAME = "ngasiryuk_database"

    /**
     * Export database ke folder Downloads
     * Support Android 10+ (API 29+) dengan MediaStore
     * @return File yang berhasil di-export atau null jika gagal
     */
    suspend fun exportDatabase(context: Context): File? = withContext(Dispatchers.IO) {
        try {
            // Path database internal
            val dbPath = context.getDatabasePath(DATABASE_NAME)

            if (!dbPath.exists()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Database tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
                return@withContext null
            }

            // Buat nama file dengan timestamp
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "ngasiryuk_backup_$timestamp.db"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ - Gunakan MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/NgasiryukBackup")
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        FileInputStream(dbPath).use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "✅ Database berhasil di-export!\n📁 Downloads/NgasiryukBackup/$fileName",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    // Return file reference (untuk sharing)
                    return@withContext File(context.getExternalFilesDir(null), "temp_$fileName").apply {
                        FileInputStream(dbPath).use { input ->
                            FileOutputStream(this).use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                } else {
                    throw Exception("Gagal membuat file di MediaStore")
                }
            } else {
                // Android 9 dan lebih lama - Gunakan External Storage
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val backupFolder = File(downloadsDir, "NgasiryukBackup")

                if (!backupFolder.exists()) {
                    backupFolder.mkdirs()
                }

                val backupFile = File(backupFolder, fileName)

                FileInputStream(dbPath).use { input ->
                    FileOutputStream(backupFile).use { output ->
                        input.copyTo(output)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "✅ Database berhasil di-export!\n📁 Downloads/NgasiryukBackup/$fileName",
                        Toast.LENGTH_LONG
                    ).show()
                }

                return@withContext backupFile
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "❌ Gagal export database: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            return@withContext null
        }
    }

    /**
     * Import database dari file
     * @param fileUri URI dari file database yang akan di-import
     * @return Boolean menandakan sukses atau gagal
     */
    suspend fun importDatabase(context: Context, fileUri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            // Tutup database terlebih dahulu
            try {
                AppDatabase.getDatabase(context).close()
            } catch (e: Exception) {
                android.util.Log.e("DatabaseManager", "Error closing database", e)
            }

            // Path database internal
            val dbPath = context.getDatabasePath(DATABASE_NAME)

            // Backup database lama terlebih dahulu
            if (dbPath.exists()) {
                val backupFile = File(dbPath.parent, "${DATABASE_NAME}_old")
                dbPath.copyTo(backupFile, overwrite = true)
            }

            // Copy file yang di-import ke database path
            context.contentResolver.openInputStream(fileUri)?.use { input ->
                FileOutputStream(dbPath).use { output ->
                    input.copyTo(output)
                }
            } ?: throw Exception("Tidak dapat membaca file")

            // Re-open database dan re-initialize AppContainer
            try {
                AppDatabase.getDatabase(context)
                // Re-initialize AppContainer untuk update repository references
                com.example.ngasiryuk.di.AppContainer.initialize(context)
            } catch (e: Exception) {
                android.util.Log.e("DatabaseManager", "Error re-initializing after import", e)
                // Restore backup jika ada error
                val backupFile = File(dbPath.parent, "${DATABASE_NAME}_old")
                if (backupFile.exists()) {
                    backupFile.copyTo(dbPath, overwrite = true)
                }
                throw e
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "✅ Database berhasil di-import!\nAplikasi akan restart...",
                    Toast.LENGTH_SHORT
                ).show()
            }

            return@withContext true
        } catch (e: Exception) {
            android.util.Log.e("DatabaseManager", "Error importing database", e)

            // Restore backup jika ada
            val dbPath = context.getDatabasePath(DATABASE_NAME)
            val backupFile = File(dbPath.parent, "${DATABASE_NAME}_old")
            if (backupFile.exists()) {
                try {
                    backupFile.copyTo(dbPath, overwrite = true)
                    // Re-initialize setelah restore
                    AppDatabase.getDatabase(context)
                    com.example.ngasiryuk.di.AppContainer.initialize(context)
                } catch (restoreError: Exception) {
                    android.util.Log.e("DatabaseManager", "Error restoring backup", restoreError)
                }
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "❌ Gagal import database: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            return@withContext false
        }
    }

    /**
     * Reset database - hapus semua data
     * @return Boolean menandakan sukses atau gagal
     */
    suspend fun resetDatabase(context: Context): Boolean = withContext(Dispatchers.IO) {
        try {
            // Tutup database
            try {
                AppDatabase.getDatabase(context).close()
            } catch (e: Exception) {
                android.util.Log.e("DatabaseManager", "Error closing database", e)
            }

            // Hapus database file
            val dbPath = context.getDatabasePath(DATABASE_NAME)
            val deleted = dbPath.delete()

            // Hapus juga file-file terkait (shm, wal)
            val shmFile = File(dbPath.parent, "$DATABASE_NAME-shm")
            val walFile = File(dbPath.parent, "$DATABASE_NAME-wal")

            shmFile.delete()
            walFile.delete()

            if (deleted) {
                // Re-initialize database dan AppContainer
                try {
                    AppDatabase.getDatabase(context)
                    // Re-initialize AppContainer untuk update repository references
                    com.example.ngasiryuk.di.AppContainer.initialize(context)
                } catch (e: Exception) {
                    android.util.Log.e("DatabaseManager", "Error re-initializing database", e)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "✅ Database berhasil di-reset!\nAplikasi akan restart...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@withContext true
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "❌ Gagal menghapus database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@withContext false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "❌ Gagal reset database: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            return@withContext false
        }
    }

    /**
     * Get URI dari file untuk sharing
     */
    fun getFileUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}






