package com.example.ngasiryuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val nomorWa: String,
    val alamat: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

