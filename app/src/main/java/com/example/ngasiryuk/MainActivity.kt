package com.example.ngasiryuk

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.ngasiryuk.navigation.AppNavHost
import com.example.ngasiryuk.screen.splashscreen.ui.theme.NgasiryukTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase
        try {
            FirebaseApp.initializeApp(this)
            Log.d("MainActivity", "✅ Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("MainActivity", "❌ Firebase initialization failed: ${e.message}", e)
        }

        // Get initial route from intent
        val initialRoute = intent.getStringExtra("INITIAL_ROUTE") ?: "daftar_toko"
        Log.d("MainActivity", "Initial route: $initialRoute")

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



