package com.example.ngasiryuk.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Migration from version 3 to 4 - Add Kasir table
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create kasir table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS kasir (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                namaKasir TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """.trimIndent())
    }
}

// Template untuk migration selanjutnya
// val MIGRATION_4_5 = object : Migration(4, 5) {
//     override fun migrate(database: SupportSQLiteDatabase) {
//         // Add your migration code here
//     }
// }

