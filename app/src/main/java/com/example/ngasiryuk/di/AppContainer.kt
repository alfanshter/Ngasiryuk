package com.example.ngasiryuk.di

import android.content.Context
import com.example.ngasiryuk.data.local.database.AppDatabase
import com.example.ngasiryuk.data.repository.CustomerRepository
import com.example.ngasiryuk.data.repository.CustomerRepositoryImpl
import com.example.ngasiryuk.data.repository.KasirRepository
import com.example.ngasiryuk.data.repository.KasirRepositoryImpl
import com.example.ngasiryuk.data.repository.KategoriRepositoryImpl
import com.example.ngasiryuk.data.repository.MenuPasswordRepository
import com.example.ngasiryuk.data.repository.TokoRepositoryImpl
import com.example.ngasiryuk.data.repository.KategoriRepository
import com.example.ngasiryuk.data.repository.ProdukRepository
import com.example.ngasiryuk.data.repository.ProdukRepositoryImpl
import com.example.ngasiryuk.data.repository.RiwayatStokRepository
import com.example.ngasiryuk.data.repository.RiwayatStokRepositoryImpl
import com.example.ngasiryuk.data.repository.TransaksiRepository
import com.example.ngasiryuk.data.repository.TransaksiRepositoryImpl
import com.example.ngasiryuk.domain.repository.TokoRepository
import com.example.ngasiryuk.domain.usecase.CheckTokoExistsUseCase
import com.example.ngasiryuk.domain.usecase.GetTokoUseCase
import com.example.ngasiryuk.domain.usecase.SaveTokoUseCase
import com.example.ngasiryuk.domain.usecase.UpdateTokoUseCase
import com.example.ngasiryuk.screen.menu.daftarkasir.DaftarKasirViewModel
import com.example.ngasiryuk.screen.menu.daftartoko.DaftarTokoViewModel
import com.example.ngasiryuk.screen.menu.dashboard.DashboardViewModel
import com.example.ngasiryuk.screen.menu.kasir.KasirViewModel
import com.example.ngasiryuk.screen.menu.kategori.ListKategoriViewModel
import com.example.ngasiryuk.screen.menu.kelolaproduk.KelolaProdukViewModel
import com.example.ngasiryuk.screen.menu.listcustomer.ListCustomerViewModel
import com.example.ngasiryuk.screen.menu.manajemenstok.ManajemenStokViewModel
import com.example.ngasiryuk.screen.menu.rekappenjualan.RekapPenjualanViewModel
import com.example.ngasiryuk.viewmodel.MenuPasswordViewModel

object AppContainer {

    private lateinit var database: AppDatabase
    private lateinit var tokoRepository: TokoRepository
    private lateinit var kategoriRepository: KategoriRepository
    private lateinit var produkRepository: ProdukRepository
    private lateinit var riwayatStokRepository: RiwayatStokRepository
    private lateinit var kasirRepository: KasirRepository
    private lateinit var customerRepository: CustomerRepository
    private lateinit var transaksiRepository: TransaksiRepository
    private lateinit var menuPasswordRepository: MenuPasswordRepository

    fun initialize(context: Context) {
        database = AppDatabase.getDatabase(context)
        tokoRepository = TokoRepositoryImpl(database.tokoDao())
        kategoriRepository = KategoriRepositoryImpl(database.kategoriDao())
        produkRepository = ProdukRepositoryImpl(database.produkDao(), database.riwayatStokDao())
        riwayatStokRepository = RiwayatStokRepositoryImpl(database.riwayatStokDao())
        kasirRepository = KasirRepositoryImpl(database.kasirDao())
        customerRepository = CustomerRepositoryImpl(database.customerDao())
        transaksiRepository = TransaksiRepositoryImpl(
            database.transaksiDao(),
            database.detailTransaksiDao(),
            database.produkDao()
        )
        menuPasswordRepository = MenuPasswordRepository(database.menuPasswordDao())
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

    fun provideKelolaProdukViewModel(): KelolaProdukViewModel {
        return KelolaProdukViewModel(
            produkRepository = produkRepository,
            riwayatStokRepository = riwayatStokRepository,
            kategoriRepository = kategoriRepository
        )
    }

    fun provideManajemenStokViewModel(): ManajemenStokViewModel {
        return ManajemenStokViewModel(
            produkRepository = produkRepository,
            riwayatStokRepository = riwayatStokRepository
        )
    }

    fun provideDaftarKasirViewModel(): DaftarKasirViewModel {
        return DaftarKasirViewModel(
            kasirRepository = kasirRepository
        )
    }

    fun provideListCustomerViewModel(): ListCustomerViewModel {
        return ListCustomerViewModel(
            customerRepository = customerRepository
        )
    }

    fun provideKasirViewModel(): KasirViewModel {
        return KasirViewModel(
            kasirRepository = kasirRepository,
            customerRepository = customerRepository,
            produkRepository = produkRepository,
            transaksiRepository = transaksiRepository
        )
    }

    fun provideRekapPenjualanViewModel(): RekapPenjualanViewModel {
        return RekapPenjualanViewModel(
            transaksiRepository = transaksiRepository
        )
    }

    fun provideMenuPasswordViewModel(): MenuPasswordViewModel {
        return MenuPasswordViewModel(
            repository = menuPasswordRepository
        )
    }
}
