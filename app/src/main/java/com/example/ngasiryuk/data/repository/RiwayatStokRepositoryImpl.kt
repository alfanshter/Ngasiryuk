package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.dao.RiwayatStokDao
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import kotlinx.coroutines.flow.Flow

class RiwayatStokRepositoryImpl(
    private val riwayatStokDao: RiwayatStokDao
) : RiwayatStokRepository {

    override fun getAllRiwayat(): Flow<List<RiwayatStokEntity>> {
        return riwayatStokDao.getAllRiwayat()
    }

    override fun getRiwayatByProduk(produkId: Int): Flow<List<RiwayatStokEntity>> {
        return riwayatStokDao.getRiwayatByProduk(produkId)
    }

    override suspend fun insertRiwayat(riwayat: RiwayatStokEntity): Long {
        return riwayatStokDao.insertRiwayat(riwayat)
    }
}

