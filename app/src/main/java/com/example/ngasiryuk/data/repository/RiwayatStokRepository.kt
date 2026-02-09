package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import kotlinx.coroutines.flow.Flow

interface RiwayatStokRepository {
    fun getAllRiwayat(): Flow<List<RiwayatStokEntity>>
    fun getRiwayatByProduk(produkId: Int): Flow<List<RiwayatStokEntity>>
    suspend fun insertRiwayat(riwayat: RiwayatStokEntity): Long
}

