package com.example.ngasiryuk.data.local.dao

import androidx.room.*
import com.example.ngasiryuk.data.local.entity.KategoriEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KategoriDao {

    @Query("SELECT * FROM kategori ORDER BY createdAt DESC")
    fun getAllKategori(): Flow<List<KategoriEntity>>

    @Query("SELECT * FROM kategori WHERE id = :id")
    suspend fun getKategoriById(id: Int): KategoriEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKategori(kategori: KategoriEntity): Long

    @Update
    suspend fun updateKategori(kategori: KategoriEntity)

    @Delete
    suspend fun deleteKategori(kategori: KategoriEntity)

    @Query("DELETE FROM kategori")
    suspend fun deleteAllKategori()
}

