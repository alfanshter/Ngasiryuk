package com.example.ngasiryuk.data.local.dao

import androidx.room.*
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RiwayatStokDao {

    @Query("SELECT * FROM riwayat_stok ORDER BY createdAt DESC")
    fun getAllRiwayat(): Flow<List<RiwayatStokEntity>>

    @Query("SELECT * FROM riwayat_stok WHERE produkId = :produkId ORDER BY createdAt DESC")
    fun getRiwayatByProduk(produkId: Int): Flow<List<RiwayatStokEntity>>

    @Query("SELECT * FROM riwayat_stok WHERE id = :id")
    suspend fun getRiwayatById(id: Int): RiwayatStokEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRiwayat(riwayat: RiwayatStokEntity): Long

    @Query("DELETE FROM riwayat_stok")
    suspend fun deleteAllRiwayat()
}

