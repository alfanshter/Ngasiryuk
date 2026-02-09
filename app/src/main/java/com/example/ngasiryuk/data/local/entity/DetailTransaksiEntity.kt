package com.example.ngasiryuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_transaksi")
data class DetailTransaksiEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val transaksiId: Int,
    val produkId: Int,
    val namaProduk: String,
    val sku: String,
    val hargaBeli: Int,
    val hargaJual: Int,
    val jumlah: Int,
    val subtotal: Int, // hargaJual * jumlah
    val labaBersih: Int, // (hargaJual - hargaBeli) * jumlah
    val createdAt: Long = System.currentTimeMillis()
)

