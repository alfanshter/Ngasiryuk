package com.example.ngasiryuk.screen.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.ngasiryuk.MainActivity
import com.example.ngasiryuk.screen.splashscreen.ui.theme.NgasiryukTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : ComponentActivity() {
    private var navigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install SplashScreen API
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NgasiryukTheme {
                SplashScreen()
            }
        }

        // Tampilkan splash sebentar lalu buka MainActivity
        lifecycleScope.launch {
            delay(1500L)
            if (!isFinishing && !navigated) {
                navigated = true
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NgasiryukTheme {
        SplashScreen()
    }
}

