package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.dao.ProdukDao
import com.example.ngasiryuk.data.local.dao.RiwayatStokDao
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import kotlinx.coroutines.flow.Flow

class ProdukRepositoryImpl(
    private val produkDao: ProdukDao,
    private val riwayatStokDao: RiwayatStokDao
) : ProdukRepository {

    override fun getAllProduk(): Flow<List<ProdukEntity>> {
        return produkDao.getAllProduk()
    }

    override suspend fun getProdukById(id: Int): ProdukEntity? {
        return produkDao.getProdukById(id)
    }

    override suspend fun getProdukBySku(sku: String): ProdukEntity? {
        return produkDao.getProdukBySku(sku)
    }

    override fun getProdukByKategori(kategoriId: Int): Flow<List<ProdukEntity>> {
        return produkDao.getProdukByKategori(kategoriId)
    }

    override suspend fun insertProduk(produk: ProdukEntity): Long {
        val id = produkDao.insertProduk(produk)

        // Catat riwayat stok masuk jika stok > 0
        if (produk.stok > 0) {
            val riwayat = RiwayatStokEntity(
                produkId = id.toInt(),
                namaProduk = produk.namaProduk,
                jumlah = produk.stok,
                keterangan = "Stok Awal",
                stokSebelum = 0,
                stokSesudah = produk.stok
            )
            riwayatStokDao.insertRiwayat(riwayat)
        }

        return id
    }

    override suspend fun updateProduk(produk: ProdukEntity) {
        produkDao.updateProduk(produk)
    }

    override suspend fun deleteProduk(produk: ProdukEntity) {
        produkDao.deleteProduk(produk)
    }

    override suspend fun updateStokProduk(produkId: Int, jumlah: Int, keterangan: String) {
        val produk = produkDao.getProdukById(produkId) ?: return
        val stokBaru = produk.stok + jumlah

        // Update stok produk
        produkDao.updateStok(produkId, jumlah, System.currentTimeMillis())

        // Update status produk
        val status = when {
            stokBaru <= 0 -> "Out of Stock"
            stokBaru < 5 -> "Low Stock"
            else -> "Ready"
        }

        val updatedProduk = produk.copy(
            stok = stokBaru,
            status = status,
            updatedAt = System.currentTimeMillis()
        )
        produkDao.updateProduk(updatedProduk)

        // Catat riwayat
        val riwayat = RiwayatStokEntity(
            produkId = produkId,
            namaProduk = produk.namaProduk,
            jumlah = jumlah,
            keterangan = keterangan,
            stokSebelum = produk.stok,
            stokSesudah = stokBaru
        )
        riwayatStokDao.insertRiwayat(riwayat)
    }
}

