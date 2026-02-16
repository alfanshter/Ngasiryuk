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

// Migration from version 6 to 7 - Add MenuPassword table
val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create menu_password table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS menu_password (
                menuName TEXT PRIMARY KEY NOT NULL,
                password TEXT NOT NULL,
                isEnabled INTEGER NOT NULL DEFAULT 1
            )
        """.trimIndent())
    }
}

// Migration from version 7 to 8 - Add Device table
val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create device table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS device (
                deviceId TEXT PRIMARY KEY NOT NULL,
                deviceName TEXT NOT NULL,
                registeredAt INTEGER NOT NULL,
                lastAccess INTEGER NOT NULL
            )
        """.trimIndent())
    }
}

// Template untuk migration selanjutnya
// val MIGRATION_8_9 = object : Migration(8, 9) {
//     override fun migrate(database: SupportSQLiteDatabase) {
//         // Add your migration code here
//     }
// }

