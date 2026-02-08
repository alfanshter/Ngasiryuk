package com.example.ngasiryuk.domain.usecase

import com.example.ngasiryuk.domain.model.Toko
import com.example.ngasiryuk.domain.repository.TokoRepository

class SaveTokoUseCase(
    private val repository: TokoRepository
) {
    suspend operator fun invoke(toko: Toko): Long {
        return repository.insertToko(toko)
    }
}

