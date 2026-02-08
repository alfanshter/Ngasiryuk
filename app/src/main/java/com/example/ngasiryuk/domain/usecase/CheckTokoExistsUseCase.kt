package com.example.ngasiryuk.domain.usecase

import com.example.ngasiryuk.domain.repository.TokoRepository

class CheckTokoExistsUseCase(
    private val repository: TokoRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isTokoExists()
    }
}

