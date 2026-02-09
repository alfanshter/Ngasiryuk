package com.example.ngasiryuk.screen.menu.kasir

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ngasiryuk.data.local.entity.CustomerEntity
import com.example.ngasiryuk.data.local.entity.KasirEntity
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import com.example.ngasiryuk.data.repository.CustomerRepository
import com.example.ngasiryuk.data.repository.KasirRepository
import com.example.ngasiryuk.data.repository.ProdukRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KasirViewModel(
    private val kasirRepository: KasirRepository,
    private val customerRepository: CustomerRepository,
    private val produkRepository: ProdukRepository
) : ViewModel() {

    private val _kasirList = MutableStateFlow<List<KasirEntity>>(emptyList())
    val kasirList: StateFlow<List<KasirEntity>> = _kasirList.asStateFlow()

    private val _customerList = MutableStateFlow<List<CustomerEntity>>(emptyList())
    val customerList: StateFlow<List<CustomerEntity>> = _customerList.asStateFlow()

    private val _produkList = MutableStateFlow<List<ProdukEntity>>(emptyList())
    val produkList: StateFlow<List<ProdukEntity>> = _produkList.asStateFlow()

    private val _selectedKasir = MutableStateFlow<KasirEntity?>(null)
    val selectedKasir: StateFlow<KasirEntity?> = _selectedKasir.asStateFlow()

    private val _selectedCustomer = MutableStateFlow<CustomerEntity?>(null)
    val selectedCustomer: StateFlow<CustomerEntity?> = _selectedCustomer.asStateFlow()

    private val _keranjangItems = MutableStateFlow<List<KeranjangItem>>(emptyList())
    val keranjangItems: StateFlow<List<KeranjangItem>> = _keranjangItems.asStateFlow()

    // Total transaksi yang reaktif terhadap perubahan keranjang
    val totalTransaksi: StateFlow<Int> = _keranjangItems.map { items ->
        items.sumOf { it.harga * it.jumlah }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Load Kasir
                kasirRepository.getAllKasir().collect { kasirList ->
                    _kasirList.value = kasirList
                    if (_selectedKasir.value == null && kasirList.isNotEmpty()) {
                        _selectedKasir.value = kasirList.first()
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading kasir: ${e.message}"
            }
        }

        viewModelScope.launch {
            try {
                // Load Customer
                customerRepository.getAllCustomer().collect { customerList ->
                    _customerList.value = customerList
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading customer: ${e.message}"
            }
        }

        viewModelScope.launch {
            try {
                // Load Produk
                produkRepository.getAllProduk().collect { produkList ->
                    _produkList.value = produkList
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading produk: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectKasir(kasir: KasirEntity) {
        _selectedKasir.value = kasir
    }

    fun selectCustomer(customer: CustomerEntity?) {
        _selectedCustomer.value = customer
    }

    fun getProdukByBarcode(barcode: String): ProdukEntity? {
        return _produkList.value.find { it.sku == barcode }
    }

    fun addToKeranjang(produk: ProdukEntity, jumlah: Int) {
        val currentItems = _keranjangItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.produkId == produk.id }

        if (existingItemIndex != -1) {
            // Update jumlah jika produk sudah ada
            val existingItem = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = existingItem.copy(jumlah = existingItem.jumlah + jumlah)
        } else {
            // Tambah produk baru
            currentItems.add(
                KeranjangItem(
                    produkId = produk.id,
                    nama = produk.namaProduk,
                    sku = produk.sku,
                    harga = produk.hargaJual.toInt(),
                    jumlah = jumlah,
                    stok = produk.stok
                )
            )
        }
        _keranjangItems.value = currentItems
    }

    fun updateKeranjangItemJumlah(index: Int, newJumlah: Int) {
        val currentItems = _keranjangItems.value.toMutableList()
        if (index in currentItems.indices) {
            val item = currentItems[index]
            if (newJumlah <= item.stok) {
                currentItems[index] = item.copy(jumlah = newJumlah)
                _keranjangItems.value = currentItems
            } else {
                _errorMessage.value = "Stok tidak mencukupi. Stok tersedia: ${item.stok}"
            }
        }
    }

    fun removeFromKeranjang(index: Int) {
        val currentItems = _keranjangItems.value.toMutableList()
        if (index in currentItems.indices) {
            currentItems.removeAt(index)
            _keranjangItems.value = currentItems
        }
    }

    fun clearKeranjang() {
        _keranjangItems.value = emptyList()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun addCustomer(nama: String, nomorWa: String, alamat: String) {
        viewModelScope.launch {
            try {
                customerRepository.insertCustomer(nama, nomorWa, alamat)
            } catch (e: Exception) {
                _errorMessage.value = "Error adding customer: ${e.message}"
            }
        }
    }

    fun addKasir(namaKasir: String) {
        viewModelScope.launch {
            try {
                kasirRepository.insertKasir(namaKasir)
            } catch (e: Exception) {
                _errorMessage.value = "Error adding kasir: ${e.message}"
            }
        }
    }
}

// Data class untuk item keranjang
data class KeranjangItem(
    val produkId: Int,
    val nama: String,
    val sku: String,
    val harga: Int,
    val jumlah: Int,
    val stok: Int
)







