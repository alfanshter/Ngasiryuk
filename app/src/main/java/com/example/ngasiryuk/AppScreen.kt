package com.example.ngasiryuk

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class AppScreen(@StringRes val title: Int, @DrawableRes val icon: Int, val route: String) {
    object SplashScreen : AppScreen(R.string.app_name, R.drawable.ic_launcher_background, "splash")
    object DaftarToko : AppScreen(R.string.app_name, R.drawable.ic_launcher_background, "daftar_toko")
    object DaftarKasir : AppScreen(R.string.app_name, R.drawable.ic_launcher_background, "daftar_kasir")
    object Dashboard : AppScreen(R.string.dashboard, R.drawable.ic_launcher_background, "dashboard")
    object Kasir : AppScreen(R.string.app_name, R.drawable.ic_launcher_background, "kasir")
    object KelolaProduk : AppScreen(R.string.app_name, R.drawable.ic_launcher_background, "kelola_produk")
    object ManajemenStok : AppScreen(R.string.app_name, R.drawable.ic_launcher_background, "manajemen_stok")
    object RekapPenjualan : AppScreen(R.string.app_name, R.drawable.ic_launcher_background, "rekap_penjualan")
    object ListKategori : AppScreen(R.string.app_name, R.drawable.ic_launcher_background, "list_kategori")
    object ListCustomer : AppScreen(R.string.app_name, R.drawable.ic_launcher_background, "list_customer")
    object PengaturanPassword : AppScreen(R.string.app_name, R.drawable.ic_launcher_background, "pengaturan_password")
}

