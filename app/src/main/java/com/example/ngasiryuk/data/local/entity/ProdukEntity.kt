package com.example.ngasiryuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produk")
data class ProdukEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaProduk: String,
    val sku: String,
    val stok: Int,
    val kategoriId: Int,
    val kategoriNama: String, // Denormalized untuk kemudahan
    val hargaBeli: Double,
    val hargaJual: Double,
    val status: String = "Ready", // Ready, Low Stock, Out of Stock
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

