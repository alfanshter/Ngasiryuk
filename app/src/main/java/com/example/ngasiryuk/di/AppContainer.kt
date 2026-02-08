package com.example.ngasiryuk.di

import android.content.Context
import com.example.ngasiryuk.data.local.database.AppDatabase
import com.example.ngasiryuk.data.repository.KategoriRepositoryImpl
import com.example.ngasiryuk.data.repository.TokoRepositoryImpl
import com.example.ngasiryuk.data.repository.KategoriRepository
import com.example.ngasiryuk.domain.repository.TokoRepository
import com.example.ngasiryuk.domain.usecase.CheckTokoExistsUseCase
import com.example.ngasiryuk.domain.usecase.GetTokoUseCase
import com.example.ngasiryuk.domain.usecase.SaveTokoUseCase
import com.example.ngasiryuk.domain.usecase.UpdateTokoUseCase
import com.example.ngasiryuk.screen.menu.daftartoko.DaftarTokoViewModel
import com.example.ngasiryuk.screen.menu.dashboard.DashboardViewModel
import com.example.ngasiryuk.screen.menu.kategori.ListKategoriViewModel

object AppContainer {

    private lateinit var database: AppDatabase
    private lateinit var tokoRepository: TokoRepository
    private lateinit var kategoriRepository: KategoriRepository

    fun initialize(context: Context) {
        database = AppDatabase.getDatabase(context)
        tokoRepository = TokoRepositoryImpl(database.tokoDao())
        kategoriRepository = KategoriRepositoryImpl(database.kategoriDao())
    }

    fun provideGetTokoUseCase(): GetTokoUseCase {
        return GetTokoUseCase(tokoRepository)
    }

    fun provideSaveTokoUseCase(): SaveTokoUseCase {
        return SaveTokoUseCase(tokoRepository)
    }

    fun provideUpdateTokoUseCase(): UpdateTokoUseCase {
        return UpdateTokoUseCase(tokoRepository)
    }

    fun provideCheckTokoExistsUseCase(): CheckTokoExistsUseCase {
        return CheckTokoExistsUseCase(tokoRepository)
    }

    fun provideDaftarTokoViewModel(): DaftarTokoViewModel {
        return DaftarTokoViewModel(
            getTokoUseCase = provideGetTokoUseCase(),
            saveTokoUseCase = provideSaveTokoUseCase(),
            updateTokoUseCase = provideUpdateTokoUseCase()
        )
    }

    fun provideDashboardViewModel(): DashboardViewModel {
        return DashboardViewModel(
            getTokoUseCase = provideGetTokoUseCase()
        )
    }

    fun provideListKategoriViewModel(): ListKategoriViewModel {
        return ListKategoriViewModel(
            kategoriRepository = kategoriRepository
        )
    }
}



