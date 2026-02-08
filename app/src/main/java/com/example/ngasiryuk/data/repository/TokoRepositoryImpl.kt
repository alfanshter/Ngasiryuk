package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.dao.TokoDao
import com.example.ngasiryuk.data.local.entity.TokoEntity
import com.example.ngasiryuk.domain.model.Toko
import com.example.ngasiryuk.domain.repository.TokoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokoRepositoryImpl(
    private val tokoDao: TokoDao
) : TokoRepository {

    override fun getToko(): Flow<Toko?> {
        return tokoDao.getToko().map { it?.toToko() }
    }

    override suspend fun getTokoById(id: Int): Toko? {
        return tokoDao.getTokoById(id)?.toToko()
    }

    override suspend fun insertToko(toko: Toko): Long {
        return tokoDao.insertToko(toko.toEntity())
    }

    override suspend fun updateToko(toko: Toko) {
        tokoDao.updateToko(toko.toEntity())
    }

    override suspend fun deleteToko(toko: Toko) {
        tokoDao.deleteToko(toko.toEntity())
    }

    override suspend fun deleteAllToko() {
        tokoDao.deleteAllToko()
    }

    override suspend fun isTokoExists(): Boolean {
        return tokoDao.isTokoExists()
    }

    // Mapper functions
    private fun TokoEntity.toToko(): Toko {
        return Toko(
            id = id,
            namaToko = namaToko,
            alamatToko = alamatToko,
            logoPath = logoPath,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun Toko.toEntity(): TokoEntity {
        return TokoEntity(
            id = id,
            namaToko = namaToko,
            alamatToko = alamatToko,
            logoPath = logoPath,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

