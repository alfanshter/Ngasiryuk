package com.example.ngasiryuk.screen.menu.daftarkasir

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ngasiryuk.data.local.entity.KasirEntity
import com.example.ngasiryuk.data.repository.KasirRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DaftarKasirViewModel(
    private val kasirRepository: KasirRepository
) : ViewModel() {

    private val _kasirList = MutableStateFlow<List<KasirEntity>>(emptyList())
    val kasirList: StateFlow<List<KasirEntity>> = _kasirList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadKasirList()
    }

    private fun loadKasirList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                kasirRepository.getAllKasir().collect { kasirList ->
                    _kasirList.value = kasirList
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading kasir: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addKasir(namaKasir: String) {
        viewModelScope.launch {
            try {
                kasirRepository.insertKasir(namaKasir)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error adding kasir: ${e.message}"
            }
        }
    }

    fun updateKasir(id: Int, namaKasir: String) {
        viewModelScope.launch {
            try {
                kasirRepository.updateKasir(id, namaKasir)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error updating kasir: ${e.message}"
            }
        }
    }

    fun deleteKasir(kasir: KasirEntity) {
        viewModelScope.launch {
            try {
                kasirRepository.deleteKasir(kasir)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting kasir: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

