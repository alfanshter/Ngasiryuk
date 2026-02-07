package com.example.ngasiryuk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.ngasiryuk.navigation.AppNavHost
import com.example.ngasiryuk.screen.splashscreen.ui.theme.NgasiryukTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NgasiryukTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Surface {
        AppNavHost(
            navController = navController,
            startDestination = AppScreen.DaftarToko
        )
    }
}



