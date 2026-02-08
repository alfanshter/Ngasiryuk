package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.dao.KategoriDao
import com.example.ngasiryuk.data.local.entity.KategoriEntity
import kotlinx.coroutines.flow.Flow

class KategoriRepositoryImpl(private val kategoriDao: KategoriDao) : KategoriRepository {

    override fun getAllKategori(): Flow<List<KategoriEntity>> {
        return kategoriDao.getAllKategori()
    }

    override suspend fun getKategoriById(id: Int): KategoriEntity? {
        return kategoriDao.getKategoriById(id)
    }

    override suspend fun insertKategori(namaKategori: String): Long {
        val kategori = KategoriEntity(namaKategori = namaKategori)
        return kategoriDao.insertKategori(kategori)
    }

    override suspend fun updateKategori(id: Int, namaKategori: String) {
        val existingKategori = kategoriDao.getKategoriById(id)
        if (existingKategori != null) {
            val updatedKategori = existingKategori.copy(
                namaKategori = namaKategori,
                updatedAt = System.currentTimeMillis()
            )
            kategoriDao.updateKategori(updatedKategori)
        }
    }

    override suspend fun deleteKategori(kategori: KategoriEntity) {
        kategoriDao.deleteKategori(kategori)
    }

    override suspend fun deleteAllKategori() {
        kategoriDao.deleteAllKategori()
    }
}


