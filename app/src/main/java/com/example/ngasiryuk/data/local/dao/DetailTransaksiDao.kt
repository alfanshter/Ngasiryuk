package com.example.ngasiryuk.data.local.dao

import androidx.room.*
import com.example.ngasiryuk.data.local.entity.DetailTransaksiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailTransaksiDao {

    @Query("SELECT * FROM detail_transaksi WHERE transaksiId = :transaksiId")
    fun getDetailByTransaksiId(transaksiId: Int): Flow<List<DetailTransaksiEntity>>

    @Query("SELECT * FROM detail_transaksi WHERE id = :id")
    suspend fun getDetailById(id: Int): DetailTransaksiEntity?

    @Query("""
        SELECT SUM(labaBersih) FROM detail_transaksi 
        WHERE transaksiId IN (
            SELECT id FROM transaksi 
            WHERE tanggalTransaksi >= :startDate 
            AND tanggalTransaksi <= :endDate
        )
    """)
    suspend fun getTotalLabaBersihByDateRange(startDate: Long, endDate: Long): Int?

    @Query("""
        SELECT SUM(hargaBeli * jumlah) FROM detail_transaksi 
        WHERE transaksiId IN (
            SELECT id FROM transaksi 
            WHERE tanggalTransaksi >= :startDate 
            AND tanggalTransaksi <= :endDate
        )
    """)
    suspend fun getTotalPengeluaranByDateRange(startDate: Long, endDate: Long): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetail(detail: DetailTransaksiEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDetail(details: List<DetailTransaksiEntity>)

    @Update
    suspend fun updateDetail(detail: DetailTransaksiEntity)

    @Delete
    suspend fun deleteDetail(detail: DetailTransaksiEntity)

    @Query("DELETE FROM detail_transaksi WHERE transaksiId = :transaksiId")
    suspend fun deleteDetailByTransaksiId(transaksiId: Int)

    @Query("DELETE FROM detail_transaksi")
    suspend fun deleteAllDetail()
}

