package com.example.ngasiryuk.domain.model

data class Kategori(
    val id: Int = 0,
    val namaKategori: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

