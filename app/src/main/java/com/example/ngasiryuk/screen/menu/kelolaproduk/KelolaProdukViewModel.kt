package com.example.ngasiryuk.screen.menu.kelolaproduk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ngasiryuk.data.local.entity.KategoriEntity
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import com.example.ngasiryuk.data.repository.KategoriRepository
import com.example.ngasiryuk.data.repository.ProdukRepository
import com.example.ngasiryuk.data.repository.RiwayatStokRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KelolaProdukViewModel(
    private val produkRepository: ProdukRepository,
    private val riwayatStokRepository: RiwayatStokRepository,
    private val kategoriRepository: KategoriRepository
) : ViewModel() {

    val produkList: StateFlow<List<ProdukEntity>> = produkRepository.getAllProduk()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val riwayatList: StateFlow<List<RiwayatStokEntity>> = riwayatStokRepository.getAllRiwayat()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val kategoriList: StateFlow<List<KategoriEntity>> = kategoriRepository.getAllKategori()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedProduk = MutableStateFlow<ProdukEntity?>(null)
    val selectedProduk: StateFlow<ProdukEntity?> = _selectedProduk.asStateFlow()

    fun addProduk(
        namaProduk: String,
        sku: String,
        stok: Int,
        kategoriId: Int,
        kategoriNama: String,
        hargaBeli: Double,
        hargaJual: Double
    ) {
        viewModelScope.launch {
            val status = when {
                stok <= 0 -> "Out of Stock"
                stok < 5 -> "Low Stock"
                else -> "Ready"
            }

            val produk = ProdukEntity(
                namaProduk = namaProduk,
                sku = sku,
                stok = stok,
                kategoriId = kategoriId,
                kategoriNama = kategoriNama,
                hargaBeli = hargaBeli,
                hargaJual = hargaJual,
                status = status
            )
            produkRepository.insertProduk(produk)
        }
    }

    fun updateProduk(produk: ProdukEntity) {
        viewModelScope.launch {
            val status = when {
                produk.stok <= 0 -> "Out of Stock"
                produk.stok < 5 -> "Low Stock"
                else -> "Ready"
            }
            produkRepository.updateProduk(produk.copy(
                status = status,
                updatedAt = System.currentTimeMillis()
            ))
        }
    }

    fun deleteProduk(produk: ProdukEntity) {
        viewModelScope.launch {
            produkRepository.deleteProduk(produk)
        }
    }

    fun updateStok(produkId: Int, jumlah: Int, keterangan: String) {
        viewModelScope.launch {
            produkRepository.updateStokProduk(produkId, jumlah, keterangan)
        }
    }

    fun selectProduk(produk: ProdukEntity?) {
        _selectedProduk.value = produk
    }

    suspend fun getProdukBySku(sku: String): ProdukEntity? {
        return produkRepository.getProdukBySku(sku)
    }
}

