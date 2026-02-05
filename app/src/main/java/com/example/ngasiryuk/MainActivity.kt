package com.example.ngasiryuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ngasiryuk.screen.menu.daftarkasir.DaftarKasir
import com.example.ngasiryuk.screen.pengaturan.PengaturanPassword

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaftarKasir()
        }
    }
}



