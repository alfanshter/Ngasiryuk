package com.example.ngasiryuk.domain.usecase

import com.example.ngasiryuk.domain.model.Toko
import com.example.ngasiryuk.domain.repository.TokoRepository

class UpdateTokoUseCase(
    private val repository: TokoRepository
) {
    suspend operator fun invoke(toko: Toko) {
        repository.updateToko(toko)
    }
}

