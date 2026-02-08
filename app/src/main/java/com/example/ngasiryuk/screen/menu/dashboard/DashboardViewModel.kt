package com.example.ngasiryuk.screen.menu.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ngasiryuk.domain.model.Toko
import com.example.ngasiryuk.domain.usecase.GetTokoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getTokoUseCase: GetTokoUseCase
) : ViewModel() {

    private val _toko = MutableStateFlow<Toko?>(null)
    val toko: StateFlow<Toko?> = _toko.asStateFlow()

    init {
        loadToko()
    }

    private fun loadToko() {
        viewModelScope.launch {
            getTokoUseCase().collect { tokoData ->
                _toko.value = tokoData
            }
        }
    }
}

