package com.example.ngasiryuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kategori")
data class KategoriEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaKategori: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

