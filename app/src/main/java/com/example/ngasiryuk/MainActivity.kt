package com.example.ngasiryuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.ngasiryuk.navigation.AppNavHost
import com.example.ngasiryuk.screen.splashscreen.ui.theme.NgasiryukTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get initial route from intent
        val initialRoute = intent.getStringExtra("INITIAL_ROUTE") ?: "daftar_toko"

        setContent {
            NgasiryukTheme {
                AppNavigation(initialRoute = initialRoute)
            }
        }
    }
}

@Composable
fun AppNavigation(initialRoute: String = "daftar_toko") {
    val navController = rememberNavController()

    val startDestination = when(initialRoute) {
        "dashboard" -> AppScreen.Dashboard
        else -> AppScreen.DaftarToko
    }

    Surface {
        AppNavHost(
            navController = navController,
            startDestination = startDestination
        )
    }
}



