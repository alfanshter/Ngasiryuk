package com.example.ngasiryuk.domain.usecase

import com.example.ngasiryuk.domain.model.Toko
import com.example.ngasiryuk.domain.repository.TokoRepository
import kotlinx.coroutines.flow.Flow

class GetTokoUseCase(
    private val repository: TokoRepository
) {
    operator fun invoke(): Flow<Toko?> {
        return repository.getToko()
    }
}

