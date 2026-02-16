package com.example.ngasiryuk.screen.splashscreen

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.ngasiryuk.MainActivity
import com.example.ngasiryuk.data.repository.DeviceAccessResult
import com.example.ngasiryuk.di.AppContainer
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

        // Tampilkan splash sebentar lalu cek device
        lifecycleScope.launch {
            delay(1500L)
            if (!isFinishing && !navigated) {
                navigated = true

                Log.d("SplashScreenActivity", "🚀 Starting device check...")

                // CEK DEVICE DULU (sesuai flowchart)
                val deviceRepository = AppContainer.provideDeviceRepository()
                when (val result = deviceRepository.checkDeviceAccess()) {
                    is DeviceAccessResult.Allowed -> {
                        Log.d("SplashScreenActivity", "✅ Device allowed, proceeding to app")

                        // Check if toko already registered
                        val checkTokoExistsUseCase = AppContainer.provideCheckTokoExistsUseCase()
                        val isTokoExists = checkTokoExistsUseCase()

                        if (isTokoExists) {
                            // Toko sudah ada, langsung ke dashboard (tidak perlu internet)
                            Log.d("SplashScreenActivity", "🏪 Store exists, going to dashboard (offline mode)")
                            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                                putExtra("INITIAL_ROUTE", "dashboard")
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            // Toko belum ada, perlu daftar (WAJIB INTERNET)
                            Log.d("SplashScreenActivity", "📝 No store, need to register (checking internet...)")

                            if (isInternetAvailable()) {
                                Log.d("SplashScreenActivity", "✅ Internet available, going to register store")
                                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                                    putExtra("INITIAL_ROUTE", "daftar_toko")
                                }
                                startActivity(intent)
                                finish()
                            } else {
                                Log.e("SplashScreenActivity", "❌ No internet connection for registration")
                                showNoInternetDialog()
                            }
                        }
                    }

                    is DeviceAccessResult.LimitReached -> {
                        Log.e("SplashScreenActivity", "❌ Device limit reached!")
                        showDeviceLimitDialog(result.deviceCount)
                    }

                    is DeviceAccessResult.Error -> {
                        Log.e("SplashScreenActivity", "❌ Device check error: ${result.message}")
                        showErrorDialog(result.message)
                    }

                    is DeviceAccessResult.NoStoreYet -> {
                        // Tidak akan terjadi karena sekarang tidak butuh toko untuk cek device
                        Log.d("SplashScreenActivity", "⚠️ No store yet, but allowing access")

                        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                            putExtra("INITIAL_ROUTE", "daftar_toko")
                        }
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun showDeviceLimitDialog(deviceCount: Int) {
        AlertDialog.Builder(this)
            .setTitle("⚠️ Batas Device Tercapai")
            .setMessage(
                "Aplikasi ini sudah digunakan di $deviceCount perangkat.\n\n" +
                        "Batas maksimal penggunaan adalah 2 perangkat.\n\n" +
                        "Silakan hubungi administrator untuk menghapus perangkat yang tidak digunakan."
            )
            .setCancelable(false)
            .setPositiveButton("Tutup Aplikasi") { _: DialogInterface, _: Int ->
                finish()
            }
            .show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("❌ Terjadi Kesalahan")
            .setMessage(
                "Tidak dapat memeriksa akses perangkat:\n\n$message\n\n" +
                        "Pastikan koneksi internet aktif dan coba lagi."
            )
            .setCancelable(false)
            .setPositiveButton("Coba Lagi") { _: DialogInterface, _: Int ->
                recreate() // Restart activity
            }
            .setNegativeButton("Tutup Aplikasi") { _: DialogInterface, _: Int ->
                finish()
            }
            .show()
    }

    private fun showNoInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle("📶 Koneksi Internet Diperlukan")
            .setMessage(
                "Untuk mendaftarkan toko pertama kali, aplikasi memerlukan koneksi internet.\n\n" +
                        "Silakan nyalakan Wi-Fi atau data seluler Anda, lalu coba lagi.\n\n" +
                        "Note: Setelah toko terdaftar, aplikasi dapat digunakan secara offline."
            )
            .setCancelable(false)
            .setPositiveButton("Coba Lagi") { _: DialogInterface, _: Int ->
                recreate() // Restart activity untuk cek ulang
            }
            .setNegativeButton("Tutup Aplikasi") { _: DialogInterface, _: Int ->
                finish()
            }
            .show()
    }

    /**
     * Check if internet connection is available
     */
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            networkInfo != null && networkInfo.isConnected
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

