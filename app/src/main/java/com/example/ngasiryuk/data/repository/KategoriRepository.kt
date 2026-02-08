package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.entity.KategoriEntity
import kotlinx.coroutines.flow.Flow

interface KategoriRepository {
    fun getAllKategori(): Flow<List<KategoriEntity>>
    suspend fun getKategoriById(id: Int): KategoriEntity?
    suspend fun insertKategori(namaKategori: String): Long
    suspend fun updateKategori(id: Int, namaKategori: String)
    suspend fun deleteKategori(kategori: KategoriEntity)
    suspend fun deleteAllKategori()
}

