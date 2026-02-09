package com.example.ngasiryuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "riwayat_stok")
data class RiwayatStokEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val produkId: Int,
    val namaProduk: String,
    val jumlah: Int, // Positif untuk masuk, negatif untuk keluar
    val keterangan: String, // Stok Masuk, Terjual, Retur, dll
    val stokSebelum: Int,
    val stokSesudah: Int,
    val createdAt: Long = System.currentTimeMillis()
)

