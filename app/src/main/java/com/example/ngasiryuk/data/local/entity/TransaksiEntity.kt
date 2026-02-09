package com.example.ngasiryuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi")
data class TransaksiEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val kasirId: Int,
    val namaKasir: String,
    val customerId: Int? = null,
    val namaCustomer: String? = null,
    val totalBelanja: Int,
    val diskon: Int = 0,
    val totalSetelahDiskon: Int,
    val uangDibayarkan: Int,
    val kembalian: Int,
    val metodePembayaran: String, // "Tunai", "Transfer", "QRIS"
    val keterangan: String? = null,
    val tanggalTransaksi: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

