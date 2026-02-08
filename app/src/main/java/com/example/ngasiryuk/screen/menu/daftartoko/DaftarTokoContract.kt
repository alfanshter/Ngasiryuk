package com.example.ngasiryuk.screen.menu.daftartoko

import com.example.ngasiryuk.domain.model.Toko

sealed class DaftarTokoEvent {
    data class OnNamaTokoChange(val namaToko: String) : DaftarTokoEvent()
    data class OnAlamatTokoChange(val alamatToko: String) : DaftarTokoEvent()
    data class OnLogoSelected(val logoPath: String) : DaftarTokoEvent()
    object OnSaveToko : DaftarTokoEvent()
    object OnUpdateToko : DaftarTokoEvent()
}

data class DaftarTokoState(
    val toko: Toko? = null,
    val namaToko: String = "",
    val alamatToko: String = "",
    val logoPath: String? = null,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaveSuccess: Boolean = false
)

