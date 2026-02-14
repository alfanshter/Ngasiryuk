package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.dao.DetailTransaksiDao
import com.example.ngasiryuk.data.local.dao.ProdukDao
import com.example.ngasiryuk.data.local.dao.RiwayatStokDao
import com.example.ngasiryuk.data.local.dao.TransaksiDao
import com.example.ngasiryuk.data.local.entity.DetailTransaksiEntity
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import com.example.ngasiryuk.data.local.entity.TransaksiEntity
import kotlinx.coroutines.flow.Flow

class TransaksiRepositoryImpl(
    private val transaksiDao: TransaksiDao,
    private val detailTransaksiDao: DetailTransaksiDao,
    private val produkDao: ProdukDao,
    private val riwayatStokDao: RiwayatStokDao
) : TransaksiRepository {

    override fun getAllTransaksi(): Flow<List<TransaksiEntity>> {
        return transaksiDao.getAllTransaksi()
    }

    override suspend fun getTransaksiById(id: Int): TransaksiEntity? {
        return transaksiDao.getTransaksiById(id)
    }

    override fun getTransaksiByDateRange(startDate: Long, endDate: Long): Flow<List<TransaksiEntity>> {
        return transaksiDao.getTransaksiByDateRange(startDate, endDate)
    }

    override suspend fun getTotalPemasukanByDateRange(startDate: Long, endDate: Long): Int {
        return transaksiDao.getTotalPemasukanByDateRange(startDate, endDate) ?: 0
    }

    override suspend fun getTotalPengeluaranByDateRange(startDate: Long, endDate: Long): Int {
        return detailTransaksiDao.getTotalPengeluaranByDateRange(startDate, endDate) ?: 0
    }

    override suspend fun getTotalLabaBersihByDateRange(startDate: Long, endDate: Long): Int {
        return detailTransaksiDao.getTotalLabaBersihByDateRange(startDate, endDate) ?: 0
    }

    override suspend fun getJumlahTransaksiByDateRange(startDate: Long, endDate: Long): Int {
        return transaksiDao.getJumlahTransaksiByDateRange(startDate, endDate)
    }

    override suspend fun insertTransaksiWithDetails(
        transaksi: TransaksiEntity,
        details: List<DetailTransaksiEntity>
    ): Long {
        // Insert transaksi terlebih dahulu
        val transaksiId = transaksiDao.insertTransaksi(transaksi)

        // Update detail dengan transaksiId yang baru
        val updatedDetails = details.map { it.copy(transaksiId = transaksiId.toInt()) }

        // Insert semua detail transaksi
        detailTransaksiDao.insertAllDetail(updatedDetails)

        // Update stok produk dan catat riwayat stok
        updatedDetails.forEach { detail ->
            val produk = produkDao.getProdukById(detail.produkId)
            produk?.let {
                val stokSebelum = it.stok
                val newStok = it.stok - detail.jumlah
                val newStatus = when {
                    newStok <= 0 -> "Out of Stock"
                    newStok <= 10 -> "Low Stock"
                    else -> "Ready"
                }

                // Update stok produk
                produkDao.updateProduk(
                    it.copy(
                        stok = newStok,
                        status = newStatus,
                        updatedAt = System.currentTimeMillis()
                    )
                )

                // Catat riwayat stok (Barang Keluar)
                val riwayatStok = RiwayatStokEntity(
                    produkId = detail.produkId,
                    namaProduk = detail.namaProduk,
                    jumlah = -detail.jumlah, // Negatif untuk barang keluar
                    keterangan = "Terjual - Transaksi #$transaksiId",
                    stokSebelum = stokSebelum,
                    stokSesudah = newStok,
                    createdAt = System.currentTimeMillis()
                )
                riwayatStokDao.insertRiwayat(riwayatStok)
            }
        }

        return transaksiId
    }

    override fun getDetailByTransaksiId(transaksiId: Int): Flow<List<DetailTransaksiEntity>> {
        return detailTransaksiDao.getDetailByTransaksiId(transaksiId)
    }
}


