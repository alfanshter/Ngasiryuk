package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.entity.ProdukEntity
import kotlinx.coroutines.flow.Flow

interface ProdukRepository {
    fun getAllProduk(): Flow<List<ProdukEntity>>
    suspend fun getProdukById(id: Int): ProdukEntity?
    suspend fun getProdukBySku(sku: String): ProdukEntity?
    fun getProdukByKategori(kategoriId: Int): Flow<List<ProdukEntity>>
    suspend fun insertProduk(produk: ProdukEntity): Long
    suspend fun updateProduk(produk: ProdukEntity)
    suspend fun deleteProduk(produk: ProdukEntity)
    suspend fun updateStokProduk(produkId: Int, jumlah: Int, keterangan: String)
}

