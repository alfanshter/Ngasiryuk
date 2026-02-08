package com.example.ngasiryuk.screen.menu.daftartoko

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ngasiryuk.domain.model.Toko
import com.example.ngasiryuk.domain.usecase.GetTokoUseCase
import com.example.ngasiryuk.domain.usecase.SaveTokoUseCase
import com.example.ngasiryuk.domain.usecase.UpdateTokoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DaftarTokoViewModel(
    private val getTokoUseCase: GetTokoUseCase,
    private val saveTokoUseCase: SaveTokoUseCase,
    private val updateTokoUseCase: UpdateTokoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DaftarTokoState())
    val state: StateFlow<DaftarTokoState> = _state.asStateFlow()

    init {
        loadToko()
    }

    private fun loadToko() {
        viewModelScope.launch {
            getTokoUseCase().collect { toko ->
                if (toko != null) {
                    _state.update {
                        it.copy(
                            toko = toko,
                            namaToko = toko.namaToko,
                            alamatToko = toko.alamatToko,
                            logoPath = toko.logoPath,
                            isEditMode = true
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: DaftarTokoEvent) {
        when (event) {
            is DaftarTokoEvent.OnNamaTokoChange -> {
                _state.update { it.copy(namaToko = event.namaToko, error = null) }
            }
            is DaftarTokoEvent.OnAlamatTokoChange -> {
                _state.update { it.copy(alamatToko = event.alamatToko, error = null) }
            }
            is DaftarTokoEvent.OnLogoSelected -> {
                _state.update { it.copy(logoPath = event.logoPath, error = null) }
            }
            is DaftarTokoEvent.OnSaveToko -> {
                saveToko()
            }
            is DaftarTokoEvent.OnUpdateToko -> {
                updateToko()
            }
        }
    }

    private fun saveToko() {
        viewModelScope.launch {
            val currentState = _state.value

            // Validation
            if (currentState.namaToko.isBlank()) {
                _state.update { it.copy(error = "Nama toko tidak boleh kosong") }
                return@launch
            }

            if (currentState.alamatToko.isBlank()) {
                _state.update { it.copy(error = "Alamat toko tidak boleh kosong") }
                return@launch
            }

            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val toko = Toko(
                    namaToko = currentState.namaToko,
                    alamatToko = currentState.alamatToko,
                    logoPath = currentState.logoPath
                )

                saveTokoUseCase(toko)

                _state.update {
                    it.copy(
                        isLoading = false,
                        isSaveSuccess = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Gagal menyimpan data: ${e.message}"
                    )
                }
            }
        }
    }

    private fun updateToko() {
        viewModelScope.launch {
            val currentState = _state.value

            // Validation
            if (currentState.namaToko.isBlank()) {
                _state.update { it.copy(error = "Nama toko tidak boleh kosong") }
                return@launch
            }

            if (currentState.alamatToko.isBlank()) {
                _state.update { it.copy(error = "Alamat toko tidak boleh kosong") }
                return@launch
            }

            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val toko = currentState.toko?.copy(
                    namaToko = currentState.namaToko,
                    alamatToko = currentState.alamatToko,
                    logoPath = currentState.logoPath,
                    updatedAt = System.currentTimeMillis()
                ) ?: return@launch

                updateTokoUseCase(toko)

                _state.update {
                    it.copy(
                        isLoading = false,
                        isSaveSuccess = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Gagal mengupdate data: ${e.message}"
                    )
                }
            }
        }
    }

    fun resetSaveSuccess() {
        _state.update { it.copy(isSaveSuccess = false) }
    }
}

