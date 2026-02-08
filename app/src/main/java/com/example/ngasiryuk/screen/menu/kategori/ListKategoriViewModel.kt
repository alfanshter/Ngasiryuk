package com.example.ngasiryuk.screen.menu.kategori

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ngasiryuk.data.local.entity.KategoriEntity
import com.example.ngasiryuk.data.repository.KategoriRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListKategoriViewModel(
    private val kategoriRepository: KategoriRepository
) : ViewModel() {

    val kategoriList: StateFlow<List<KategoriEntity>> = kategoriRepository.getAllKategori()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addKategori(namaKategori: String) {
        viewModelScope.launch {
            if (namaKategori.isNotEmpty()) {
                kategoriRepository.insertKategori(namaKategori)
            }
        }
    }

    fun updateKategori(id: Int, namaKategori: String) {
        viewModelScope.launch {
            if (namaKategori.isNotEmpty()) {
                kategoriRepository.updateKategori(id, namaKategori)
            }
        }
    }

    fun deleteKategori(kategori: KategoriEntity) {
        viewModelScope.launch {
            kategoriRepository.deleteKategori(kategori)
        }
    }
}

