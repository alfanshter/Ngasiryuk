package com.example.ngasiryuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ngasiryuk.screen.kasir.KasirScreen
import com.example.ngasiryuk.screen.menu.daftartoko.DaftarToko
import com.example.ngasiryuk.screen.menu.dashboard.Dashboard
import com.example.ngasiryuk.screen.menu.kategori.ListKategoriScreen
import com.example.ngasiryuk.screen.menu.kelolaproduk.KelolaProduk
import com.example.ngasiryuk.screen.menu.manajemenstok.ManajemenStok
import com.example.ngasiryuk.screen.pengaturan.PengaturanPassword
import com.example.ngasiryuk.ui.theme.NgasiryukTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PengaturanPassword()
        }
    }
}



