package com.example.ngasiryuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "toko")
data class TokoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaToko: String,
    val alamatToko: String,
    val logoPath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

