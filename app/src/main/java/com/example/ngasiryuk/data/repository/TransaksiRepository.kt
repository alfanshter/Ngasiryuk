package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.entity.DetailTransaksiEntity
import com.example.ngasiryuk.data.local.entity.TransaksiEntity
import kotlinx.coroutines.flow.Flow

interface TransaksiRepository {
    fun getAllTransaksi(): Flow<List<TransaksiEntity>>
    suspend fun getTransaksiById(id: Int): TransaksiEntity?
    fun getTransaksiByDateRange(startDate: Long, endDate: Long): Flow<List<TransaksiEntity>>
    suspend fun getTotalPemasukanByDateRange(startDate: Long, endDate: Long): Int
    suspend fun getTotalPengeluaranByDateRange(startDate: Long, endDate: Long): Int
    suspend fun getTotalLabaBersihByDateRange(startDate: Long, endDate: Long): Int
    suspend fun getJumlahTransaksiByDateRange(startDate: Long, endDate: Long): Int
    suspend fun insertTransaksiWithDetails(
        transaksi: TransaksiEntity,
        details: List<DetailTransaksiEntity>
    ): Long
    fun getDetailByTransaksiId(transaksiId: Int): Flow<List<DetailTransaksiEntity>>
}

