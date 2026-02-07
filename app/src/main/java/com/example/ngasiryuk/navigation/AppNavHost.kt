package com.example.ngasiryuk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ngasiryuk.AppScreen
import com.example.ngasiryuk.screen.menu.daftartoko.DaftarToko
import com.example.ngasiryuk.screen.menu.daftarkasir.DaftarKasir
import com.example.ngasiryuk.screen.menu.dashboard.Dashboard
import com.example.ngasiryuk.screen.menu.kasir.KasirScreen
import com.example.ngasiryuk.screen.menu.kelolaproduk.KelolaProduk
import com.example.ngasiryuk.screen.menu.manajemenstok.ManajemenStok
import com.example.ngasiryuk.screen.menu.rekappenjualan.RekapPenjualan
import com.example.ngasiryuk.screen.menu.kategori.ListKategoriScreen
import com.example.ngasiryuk.screen.menu.ListCustomer
import com.example.ngasiryuk.screen.pengaturan.PengaturanPassword

/**
 * NavHost untuk mengelola navigasi antar screen dalam aplikasi
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: AppScreen = AppScreen.DaftarToko
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        // Daftar Toko Screen
        composable(AppScreen.DaftarToko.route) {
            DaftarToko(
                navController = navController
            )
        }

        // Daftar Kasir Screen
        composable(AppScreen.DaftarKasir.route) {
            DaftarKasir(navController= navController)
        }

        // Dashboard Screen
        composable(AppScreen.Dashboard.route) {
            Dashboard(navController = navController)
        }

        // Kasir Screen
        composable(AppScreen.Kasir.route) {
            KasirScreen(navController= navController)
        }

        // Kelola Produk Screen
        composable(AppScreen.KelolaProduk.route) {
            KelolaProduk(navController = navController)
        }

        // Manajemen Stok Screen
        composable(AppScreen.ManajemenStok.route) {
            ManajemenStok(navController = navController)
        }

        // Rekap Penjualan Screen
        composable(AppScreen.RekapPenjualan.route) {
            RekapPenjualan(navController = navController)
        }

        // List Kategori Screen
        composable(AppScreen.ListKategori.route) {
            ListKategoriScreen(navController = navController)
        }

        // List Customer Screen
        composable(AppScreen.ListCustomer.route) {
            ListCustomer(navController= navController)
        }

        // Pengaturan Password Screen
        composable(AppScreen.PengaturanPassword.route) {
            PengaturanPassword(navController= navController)
        }
    }
}






