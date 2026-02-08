package com.example.ngasiryuk.domain.model

data class Toko(
    val id: Int = 0,
    val namaToko: String,
    val alamatToko: String,
    val logoPath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

