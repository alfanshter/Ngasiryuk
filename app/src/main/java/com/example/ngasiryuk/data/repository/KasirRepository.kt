package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.entity.KasirEntity
import kotlinx.coroutines.flow.Flow

interface KasirRepository {
    fun getAllKasir(): Flow<List<KasirEntity>>
    suspend fun getKasirById(id: Int): KasirEntity?
    suspend fun insertKasir(namaKasir: String): Long
    suspend fun updateKasir(id: Int, namaKasir: String)
    suspend fun deleteKasir(kasir: KasirEntity)
    suspend fun deleteAllKasir()
}

