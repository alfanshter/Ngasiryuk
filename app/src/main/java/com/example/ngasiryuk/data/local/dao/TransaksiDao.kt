package com.example.ngasiryuk.data.local.dao

import androidx.room.*
import com.example.ngasiryuk.data.local.entity.TransaksiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaksiDao {

    @Query("SELECT * FROM transaksi ORDER BY tanggalTransaksi DESC")
    fun getAllTransaksi(): Flow<List<TransaksiEntity>>

    @Query("SELECT * FROM transaksi WHERE id = :id")
    suspend fun getTransaksiById(id: Int): TransaksiEntity?

    @Query("""
        SELECT * FROM transaksi 
        WHERE tanggalTransaksi >= :startDate 
        AND tanggalTransaksi <= :endDate 
        ORDER BY tanggalTransaksi DESC
    """)
    fun getTransaksiByDateRange(startDate: Long, endDate: Long): Flow<List<TransaksiEntity>>

    @Query("""
        SELECT SUM(totalSetelahDiskon) FROM transaksi 
        WHERE tanggalTransaksi >= :startDate 
        AND tanggalTransaksi <= :endDate
    """)
    suspend fun getTotalPemasukanByDateRange(startDate: Long, endDate: Long): Int?

    @Query("""
        SELECT COUNT(*) FROM transaksi 
        WHERE tanggalTransaksi >= :startDate 
        AND tanggalTransaksi <= :endDate
    """)
    suspend fun getJumlahTransaksiByDateRange(startDate: Long, endDate: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksi(transaksi: TransaksiEntity): Long

    @Update
    suspend fun updateTransaksi(transaksi: TransaksiEntity)

    @Delete
    suspend fun deleteTransaksi(transaksi: TransaksiEntity)

    @Query("DELETE FROM transaksi")
    suspend fun deleteAllTransaksi()
}

