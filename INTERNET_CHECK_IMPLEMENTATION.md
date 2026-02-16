# 📶 Internet Connection Check - Implementation Guide

## 🎯 Overview
Implementasi pengecekan koneksi internet yang **HANYA WAJIB untuk Daftar Toko pertama kali**, sedangkan untuk fitur lainnya (Dashboard, Kasir, dll) bisa berjalan **offline**.

## 🔄 Alur Kerja

```
📱 Buka Aplikasi (SplashScreen)
    ↓
🔍 Cek Device di Room & Firestore
    ├─ Allowed → Lanjut
    └─ Limit Reached → ❌ Pop-up Blocker → END
    ↓
🏪 Cek Apakah Toko Sudah Terdaftar?
    │
    ├─ YA (Toko sudah ada)
    │  ├─ ✅ Tidak perlu internet
    │  └─ 🚀 Langsung ke Dashboard (Offline Mode)
    │
    └─ TIDAK (Toko belum ada)
       ├─ 📶 Cek Koneksi Internet
       │  │
       │  ├─ Ada Internet
       │  │  └─ ✅ Masuk ke Daftar Toko
       │  │
       │  └─ Tidak Ada Internet
       │     └─ ❌ Pop-up "Koneksi Internet Diperlukan"
       │        ├─ [Coba Lagi] → Restart cek ulang
       │        └─ [Tutup Aplikasi] → END
```

## ✨ Fitur

### 1. **Offline Mode untuk Toko yang Sudah Terdaftar** ✅
- Jika toko sudah pernah didaftarkan
- User bisa langsung masuk ke Dashboard
- **TIDAK PERLU INTERNET** untuk akses harian
- Semua fitur kasir, produk, transaksi berjalan offline

### 2. **Internet Wajib Hanya untuk Daftar Toko Pertama Kali** ⚠️
- Jika belum ada toko terdaftar
- **WAJIB koneksi internet** untuk registrasi device ke Firestore
- Pop-up otomatis muncul jika tidak ada internet
- User tidak bisa bypass requirement ini

### 3. **Smart Internet Detection** 🧠
Sistem mendeteksi koneksi melalui:
- ✅ Wi-Fi
- ✅ Data Seluler (4G/5G)
- ✅ Ethernet (untuk tablet/device khusus)

## 📱 Pop-up Dialog

### Dialog: "Koneksi Internet Diperlukan"
```
📶 Koneksi Internet Diperlukan

Untuk mendaftarkan toko pertama kali, aplikasi memerlukan koneksi internet.

Silakan nyalakan Wi-Fi atau data seluler Anda, lalu coba lagi.

Note: Setelah toko terdaftar, aplikasi dapat digunakan secara offline.

[Coba Lagi]  [Tutup Aplikasi]
```

**Tombol:**
- **Coba Lagi**: Restart SplashScreen, cek ulang koneksi
- **Tutup Aplikasi**: Keluar dari aplikasi

## 🔧 Implementasi Teknis

### File yang Dimodifikasi: `SplashScreenActivity.kt`

#### 1. Import yang Ditambahkan:
```kotlin
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
```

#### 2. Fungsi Baru: `isInternetAvailable()`
```kotlin
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
```

#### 3. Fungsi Baru: `showNoInternetDialog()`
```kotlin
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
            recreate() // Restart activity
        }
        .setNegativeButton("Tutup Aplikasi") { _: DialogInterface, _: Int ->
            finish()
        }
        .show()
}
```

#### 4. Logika Cek Internet dalam Device Access:
```kotlin
if (isTokoExists) {
    // Toko sudah ada → Tidak perlu internet
    Log.d("SplashScreenActivity", "🏪 Store exists, going to dashboard (offline mode)")
    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
        putExtra("INITIAL_ROUTE", "dashboard")
    }
    startActivity(intent)
    finish()
} else {
    // Toko belum ada → WAJIB internet
    Log.d("SplashScreenActivity", "📝 No store, need to register (checking internet...)")
    
    if (isInternetAvailable()) {
        // Ada internet → Lanjut ke Daftar Toko
        Log.d("SplashScreenActivity", "✅ Internet available, going to register store")
        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
            putExtra("INITIAL_ROUTE", "daftar_toko")
        }
        startActivity(intent)
        finish()
    } else {
        // Tidak ada internet → Pop-up blocker
        Log.e("SplashScreenActivity", "❌ No internet connection for registration")
        showNoInternetDialog()
    }
}
```

## 📊 Log Messages

### Toko Sudah Ada (Offline):
```
🏪 Store exists, going to dashboard (offline mode)
```

### Toko Belum Ada + Ada Internet:
```
📝 No store, need to register (checking internet...)
✅ Internet available, going to register store
```

### Toko Belum Ada + Tidak Ada Internet:
```
📝 No store, need to register (checking internet...)
❌ No internet connection for registration
```

## 🧪 Test Cases

### Test 1: First Install - No Internet ❌
**Setup:**
1. Uninstall aplikasi
2. Matikan Wi-Fi dan data seluler
3. Install aplikasi
4. Buka aplikasi

**Expected Result:**
- Pop-up "📶 Koneksi Internet Diperlukan" muncul
- Tidak bisa masuk ke aplikasi
- User harus nyalakan internet dulu

### Test 2: First Install - With Internet ✅
**Setup:**
1. Uninstall aplikasi
2. Nyalakan Wi-Fi atau data
3. Install aplikasi
4. Buka aplikasi

**Expected Result:**
- Masuk ke halaman Daftar Toko
- Bisa registrasi toko
- Device terdaftar di Firestore

### Test 3: Already Registered - No Internet ✅
**Setup:**
1. Toko sudah pernah didaftarkan
2. Matikan Wi-Fi dan data seluler
3. Buka aplikasi

**Expected Result:**
- **LANGSUNG MASUK** ke Dashboard
- **Tidak ada pop-up** internet required
- Aplikasi berjalan normal (offline mode)

### Test 4: Already Registered - With Internet ✅
**Setup:**
1. Toko sudah terdaftar
2. Internet aktif
3. Buka aplikasi

**Expected Result:**
- Langsung masuk ke Dashboard
- Aplikasi berjalan normal

## 📝 Permission Required

Pastikan permission sudah ada di `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## 💡 Keuntungan Sistem Ini

### 1. **User Experience Lebih Baik** ✨
- User tidak frustasi karena error mendadak
- Pop-up jelas menjelaskan kenapa butuh internet
- Setelah registrasi, bisa offline selamanya

### 2. **Hemat Data Internet** 💰
- Internet hanya dibutuhkan 1x saat registrasi
- Setelah itu bisa offline selamanya
- Tidak ada sync data berkala

### 3. **Cocok untuk Toko di Daerah dengan Sinyal Lemah** 📶
- Registrasi bisa di tempat dengan Wi-Fi
- Operasional harian bisa tanpa internet
- Tidak tergantung sinyal operator

### 4. **Security Device Management Tetap Jalan** 🔐
- Device tetap ter-track di Firestore
- Limit device tetap berlaku
- Hanya registrasi pertama yang butuh internet

## 🔄 Flow Comparison

### ❌ SEBELUM (Tanpa Internet Check):
```
User install → Buka app → Daftar Toko → ERROR: No Internet
                                          (User bingung kenapa error)
```

### ✅ SESUDAH (Dengan Internet Check):
```
User install → Buka app → Cek Internet → Pop-up jelas: "Nyalakan Internet dulu"
                                          (User paham apa yang harus dilakukan)
```

## 🐛 Troubleshooting

### Problem: Pop-up muncul padahal internet aktif
**Solution:**
1. Cek apakah Wi-Fi/data benar-benar terkoneksi (bukan hanya terhubung)
2. Test buka browser, pastikan internet berfungsi
3. Restart aplikasi

### Problem: Tidak bisa masuk padahal sudah nyalakan internet
**Solution:**
1. Pastikan toko belum pernah terdaftar
2. Cek log: Filter "SplashScreenActivity"
3. Pastikan Firestore rules mengizinkan access

### Problem: Toko sudah terdaftar tapi tetap minta internet
**Solution:**
1. Cek database Room apakah toko tersimpan
2. Log harus menunjukkan: "🏪 Store exists"
3. Jika tidak, berarti database corrupted, perlu reinstall

## ✅ Checklist Implementation

- [x] Import ConnectivityManager dan NetworkCapabilities
- [x] Fungsi `isInternetAvailable()` untuk cek koneksi
- [x] Dialog `showNoInternetDialog()` dengan pesan jelas
- [x] Logika kondisional: Toko ada = offline, Toko belum = wajib internet
- [x] Permission `ACCESS_NETWORK_STATE` di Manifest
- [x] Testing di 4 skenario (dengan/tanpa toko, dengan/tanpa internet)

---

**Status: IMPLEMENTED & TESTED** ✅

**Key Points:**
- ✅ Internet **HANYA** wajib untuk Daftar Toko pertama kali
- ✅ Setelah toko terdaftar, aplikasi **100% offline**
- ✅ Pop-up informatif untuk user experience yang baik
- ✅ Tidak ada error mendadak yang membingungkan user

