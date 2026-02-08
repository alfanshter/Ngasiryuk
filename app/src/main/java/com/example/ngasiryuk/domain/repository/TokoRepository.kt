package com.example.ngasiryuk.domain.repository

import com.example.ngasiryuk.domain.model.Toko
import kotlinx.coroutines.flow.Flow

interface TokoRepository {
    fun getToko(): Flow<Toko?>
    suspend fun getTokoById(id: Int): Toko?
    suspend fun insertToko(toko: Toko): Long
    suspend fun updateToko(toko: Toko)
    suspend fun deleteToko(toko: Toko)
    suspend fun deleteAllToko()
    suspend fun isTokoExists(): Boolean
}

