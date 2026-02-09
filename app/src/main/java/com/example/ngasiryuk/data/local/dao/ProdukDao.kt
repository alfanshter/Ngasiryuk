package com.example.ngasiryuk.data.local.dao

import androidx.room.*
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdukDao {

    @Query("SELECT * FROM produk ORDER BY updatedAt DESC")
    fun getAllProduk(): Flow<List<ProdukEntity>>

    @Query("SELECT * FROM produk WHERE id = :id")
    suspend fun getProdukById(id: Int): ProdukEntity?

    @Query("SELECT * FROM produk WHERE sku = :sku")
    suspend fun getProdukBySku(sku: String): ProdukEntity?

    @Query("SELECT * FROM produk WHERE kategoriId = :kategoriId")
    fun getProdukByKategori(kategoriId: Int): Flow<List<ProdukEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduk(produk: ProdukEntity): Long

    @Update
    suspend fun updateProduk(produk: ProdukEntity)

    @Delete
    suspend fun deleteProduk(produk: ProdukEntity)

    @Query("DELETE FROM produk")
    suspend fun deleteAllProduk()

    @Query("UPDATE produk SET stok = stok + :jumlah, updatedAt = :timestamp WHERE id = :produkId")
    suspend fun updateStok(produkId: Int, jumlah: Int, timestamp: Long)
}

