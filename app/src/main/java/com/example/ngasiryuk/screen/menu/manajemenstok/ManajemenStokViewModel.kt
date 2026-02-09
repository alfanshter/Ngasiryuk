package com.example.ngasiryuk.screen.menu.manajemenstok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import com.example.ngasiryuk.data.repository.ProdukRepository
import com.example.ngasiryuk.data.repository.RiwayatStokRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManajemenStokViewModel(
    private val produkRepository: ProdukRepository,
    private val riwayatStokRepository: RiwayatStokRepository
) : ViewModel() {

    // Search queries
    private val _searchQueryStok = MutableStateFlow("")
    val searchQueryStok: StateFlow<String> = _searchQueryStok

    private val _searchQueryMasuk = MutableStateFlow("")
    val searchQueryMasuk: StateFlow<String> = _searchQueryMasuk

    private val _searchQueryKeluar = MutableStateFlow("")
    val searchQueryKeluar: StateFlow<String> = _searchQueryKeluar

    // All produk list
    private val allProdukFlow = produkRepository.getAllProduk()

    // Filtered produk list based on search
    val filteredProdukList: StateFlow<List<ProdukEntity>> = combine(
        allProdukFlow,
        _searchQueryStok
    ) { produkList, query ->
        if (query.isEmpty()) {
            produkList
        } else {
            produkList.filter { produk ->
                produk.namaProduk.contains(query, ignoreCase = true) ||
                produk.sku.contains(query, ignoreCase = true) ||
                produk.kategoriNama.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // All riwayat stok
    private val allRiwayatFlow = riwayatStokRepository.getAllRiwayat()

    // Barang Masuk (jumlah > 0)
    val barangMasukList: StateFlow<List<RiwayatStokEntity>> = combine(
        allRiwayatFlow,
        _searchQueryMasuk
    ) { riwayatList, query ->
        val filtered = riwayatList.filter { it.jumlah > 0 }
        if (query.isEmpty()) {
            filtered
        } else {
            filtered.filter { riwayat ->
                riwayat.namaProduk.contains(query, ignoreCase = true) ||
                riwayat.keterangan.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Barang Keluar (jumlah < 0)
    val barangKeluarList: StateFlow<List<RiwayatStokEntity>> = combine(
        allRiwayatFlow,
        _searchQueryKeluar
    ) { riwayatList, query ->
        val filtered = riwayatList.filter { it.jumlah < 0 }
        if (query.isEmpty()) {
            filtered
        } else {
            filtered.filter { riwayat ->
                riwayat.namaProduk.contains(query, ignoreCase = true) ||
                riwayat.keterangan.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Update search queries
    fun updateSearchQueryStok(query: String) {
        _searchQueryStok.value = query
    }

    fun updateSearchQueryMasuk(query: String) {
        _searchQueryMasuk.value = query
    }

    fun updateSearchQueryKeluar(query: String) {
        _searchQueryKeluar.value = query
    }

    // Tambah stok
    fun tambahStok(produkId: Int, jumlah: Int, keterangan: String = "Stok Masuk") {
        viewModelScope.launch {
            produkRepository.updateStokProduk(produkId, jumlah, keterangan)
        }
    }

    // Kurangi stok
    fun kurangiStok(produkId: Int, jumlah: Int, keterangan: String = "Stok Keluar") {
        viewModelScope.launch {
            produkRepository.updateStokProduk(produkId, -jumlah, keterangan)
        }
    }

    // Get produk by ID
    suspend fun getProdukById(id: Int): ProdukEntity? {
        return produkRepository.getProdukById(id)
    }
}

