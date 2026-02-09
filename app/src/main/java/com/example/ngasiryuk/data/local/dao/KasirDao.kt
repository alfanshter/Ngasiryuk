package com.example.ngasiryuk.data.local.dao

import androidx.room.*
import com.example.ngasiryuk.data.local.entity.KasirEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KasirDao {

    @Query("SELECT * FROM kasir ORDER BY createdAt DESC")
    fun getAllKasir(): Flow<List<KasirEntity>>

    @Query("SELECT * FROM kasir WHERE id = :id")
    suspend fun getKasirById(id: Int): KasirEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKasir(kasir: KasirEntity): Long

    @Update
    suspend fun updateKasir(kasir: KasirEntity)

    @Delete
    suspend fun deleteKasir(kasir: KasirEntity)

    @Query("DELETE FROM kasir")
    suspend fun deleteAllKasir()
}

