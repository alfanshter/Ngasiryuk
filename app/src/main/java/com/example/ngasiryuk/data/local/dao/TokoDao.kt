package com.example.ngasiryuk.data.local.dao

import androidx.room.*
import com.example.ngasiryuk.data.local.entity.TokoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TokoDao {

    @Query("SELECT * FROM toko LIMIT 1")
    fun getToko(): Flow<TokoEntity?>

    @Query("SELECT * FROM toko WHERE id = :id")
    suspend fun getTokoById(id: Int): TokoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToko(toko: TokoEntity): Long

    @Update
    suspend fun updateToko(toko: TokoEntity)

    @Delete
    suspend fun deleteToko(toko: TokoEntity)

    @Query("DELETE FROM toko")
    suspend fun deleteAllToko()

    @Query("SELECT EXISTS(SELECT 1 FROM toko LIMIT 1)")
    suspend fun isTokoExists(): Boolean
}

