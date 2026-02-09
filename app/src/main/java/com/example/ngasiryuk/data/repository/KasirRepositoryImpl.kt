package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.dao.KasirDao
import com.example.ngasiryuk.data.local.entity.KasirEntity
import kotlinx.coroutines.flow.Flow

class KasirRepositoryImpl(private val kasirDao: KasirDao) : KasirRepository {

    override fun getAllKasir(): Flow<List<KasirEntity>> {
        return kasirDao.getAllKasir()
    }

    override suspend fun getKasirById(id: Int): KasirEntity? {
        return kasirDao.getKasirById(id)
    }

    override suspend fun insertKasir(namaKasir: String): Long {
        val kasir = KasirEntity(namaKasir = namaKasir)
        return kasirDao.insertKasir(kasir)
    }

    override suspend fun updateKasir(id: Int, namaKasir: String) {
        val existingKasir = kasirDao.getKasirById(id)
        if (existingKasir != null) {
            val updatedKasir = existingKasir.copy(
                namaKasir = namaKasir,
                updatedAt = System.currentTimeMillis()
            )
            kasirDao.updateKasir(updatedKasir)
        }
    }

    override suspend fun deleteKasir(kasir: KasirEntity) {
        kasirDao.deleteKasir(kasir)
    }

    override suspend fun deleteAllKasir() {
        kasirDao.deleteAllKasir()
    }
}

