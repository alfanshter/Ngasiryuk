package com.example.ngasiryuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kasir")
data class KasirEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaKasir: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

