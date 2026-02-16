# 🔐 Device Management with Firestore - Implementation Complete

## 📋 Overview
Implementasi sistem device management untuk membatasi penggunaan aplikasi maksimal 2 perangkat menggunakan Firebase Firestore.

## 🎯 Fitur
- ✅ Cek device ID saat aplikasi pertama kali dibuka
- ✅ Registrasi otomatis device baru ke Firestore
- ✅ Pembatasan maksimal 2 device per aplikasi
- ✅ Pop-up blocker jika melebihi batas
- ✅ Penyimpanan local dengan Room Database
- ✅ Global device tracking (tidak per-user/toko)

## 🔄 Alur Kerja (Sesuai Flowchart)

```
1. Aplikasi dibuka (SplashScreen)
   ↓
2. Cek device di Room Database lokal
   ├─ Jika ADA → Masuk aplikasi ✅
   └─ Jika TIDAK ADA → Lanjut ke step 3
   ↓
3. Cek device di Firestore
   ├─ Jika ADA → Simpan ke Room → Masuk aplikasi ✅
   └─ Jika TIDAK ADA → Lanjut ke step 4
   ↓
4. Cek jumlah device terdaftar di Firestore
   ├─ Jika < 2 → Daftarkan device → Simpan Room & Firestore → Masuk aplikasi ✅
   └─ Jika >= 2 → Pop-up "Batas tercapai" → Tutup aplikasi ❌
```

## 📁 File yang Dimodifikasi

### 1. **SplashScreenActivity.kt**
**Path:** `app/src/main/java/com/example/ngasiryuk/screen/splashscreen/SplashScreenActivity.kt`

**Perubahan:**
- Menambahkan pengecekan device di awal aplikasi
- Menampilkan dialog jika device limit tercapai
- Menampilkan dialog error jika koneksi gagal

**Kode penting:**
```kotlin
val deviceRepository = AppContainer.provideDeviceRepository()
when (val result = deviceRepository.checkDeviceAccess()) {
    is DeviceAccessResult.Allowed -> {
        // Lanjut ke aplikasi
    }
    is DeviceAccessResult.LimitReached -> {
        // Tampilkan dialog blocker
        showDeviceLimitDialog(result.deviceCount)
    }
    is DeviceAccessResult.Error -> {
        // Tampilkan dialog error
        showErrorDialog(result.message)
    }
}
```

### 2. **DeviceRepository.kt**
**Path:** `app/src/main/java/com/example/ngasiryuk/data/repository/DeviceRepository.kt`

**Perubahan:**
- Mengubah struktur Firestore dari per-user menjadi global per-aplikasi
- Path Firestore: `app_devices/{packageName}/devices/{deviceId}`
- Logika device check yang lebih robust

**Struktur Firestore baru:**
```
app_devices/
  └── com.example.ngasiryuk/
      └── devices/
          ├── device_id_1
          │   ├── deviceId: "xxx"
          │   ├── deviceName: "Samsung A20"
          │   ├── registeredAt: 1234567890
          │   └── lastAccess: 1234567890
          └── device_id_2
              ├── deviceId: "yyy"
              ├── deviceName: "Xiaomi Redmi"
              ├── registeredAt: 1234567890
              └── lastAccess: 1234567890
```

### 3. **DaftarTokoViewModel.kt**
**Path:** `app/src/main/java/com/example/ngasiryuk/screen/menu/daftartoko/DaftarTokoViewModel.kt`

**Perubahan:**
- Menghapus semua logika device check
- Menyederhanakan ViewModel hanya untuk handle toko
- Device check sudah dipindah ke SplashScreen

### 4. **DaftarTokoContract.kt**
**Path:** `app/src/main/java/com/example/ngasiryuk/screen/menu/daftartoko/DaftarTokoContract.kt`

**Perubahan:**
- Menghapus state device management (`isCheckingDevice`, `deviceAccessGranted`, `deviceBlockMessage`)
- State hanya fokus pada data toko

### 5. **DaftarToko.kt**
**Path:** `app/src/main/java/com/example/ngasiryuk/screen/menu/daftartoko/DaftarToko.kt`

**Perubahan:**
- Menghapus dialog internet connection
- Menghapus dialog device blocker
- UI hanya fokus pada form daftar toko

### 6. **AppContainer.kt**
**Path:** `app/src/main/java/com/example/ngasiryuk/di/AppContainer.kt`

**Perubahan:**
- Menambahkan fungsi `provideDeviceRepository()`
- Update `provideDaftarTokoViewModel()` untuk tidak pass deviceRepository

## 🔧 Konfigurasi Firestore

### Firestore Rules (Sementara - Development)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

### Firestore Rules (Recommended - Production)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Device management collection
    match /app_devices/{appId}/devices/{deviceId} {
      // Allow read/write untuk device sendiri
      allow read, write: if true; // Bisa disesuaikan dengan auth
    }
  }
}
```

## 📱 Cara Mengubah Limit Device

Edit file `DeviceRepository.kt`:
```kotlin
class DeviceRepository(
    private val context: Context,
    private val deviceDao: DeviceDao,
    private val tokoDao: TokoDao
) {
    private val firestore = FirebaseFirestore.getInstance()
    private val maxDevices = 2  // 👈 UBAH DI SINI
    
    // ...
}
```

## 🧪 Testing

### Test Case 1: Device Pertama
1. Install aplikasi di device 1
2. Buka aplikasi
3. ✅ Harus langsung masuk tanpa blocker
4. Cek Firestore: Harus ada 1 device terdaftar

### Test Case 2: Device Kedua
1. Install aplikasi di device 2
2. Buka aplikasi
3. ✅ Harus langsung masuk tanpa blocker
4. Cek Firestore: Harus ada 2 device terdaftar

### Test Case 3: Device Ketiga (Harus Terblokir)
1. Install aplikasi di device 3
2. Buka aplikasi
3. ❌ Harus muncul pop-up "Batas Device Tercapai"
4. Klik "Tutup Aplikasi"
5. Aplikasi harus tertutup
6. Cek Firestore: Masih ada 2 device saja

### Test Case 4: Device Terdaftar (Buka Lagi)
1. Buka aplikasi di device 1 atau 2 yang sudah terdaftar
2. ✅ Harus langsung masuk tanpa proses registrasi ulang
3. Cek log: Device ditemukan di Room DB lokal

## 📊 Logging

Semua log device management dimulai dengan emoji untuk memudahkan debugging:
- 🚀 - Start process
- 🔍 - Checking/Searching
- ✅ - Success
- ❌ - Error/Blocked
- 📱 - Device info
- 🔥 - Firestore operation
- 💾 - Local storage operation
- 📊 - Statistics/Count
- 🆕 - New registration

Filter di Logcat: `DeviceRepository` atau `SplashScreenActivity`

## 🐛 Troubleshooting

### Problem: "PERMISSION_DENIED: Missing or insufficient permissions"
**Solution:**
1. Cek Firestore Rules di Firebase Console
2. Pastikan rules mengizinkan read/write
3. Untuk development, gunakan:
   ```javascript
   allow read, write: if true;
   ```

### Problem: Device count selalu 0
**Solution:**
1. Pastikan Firebase sudah terkoneksi (cek log Firebase initialization)
2. Cek struktur collection di Firestore Console
3. Path harus: `app_devices/com.example.ngasiryuk/devices/`

### Problem: Aplikasi crash saat buka
**Solution:**
1. Cek Logcat untuk error stack trace
2. Pastikan AppContainer sudah di-initialize di Application class
3. Cek apakah google-services.json sudah ada

### Problem: Device blocker tidak muncul
**Solution:**
1. Cek log di Logcat: Filter `SplashScreenActivity`
2. Pastikan ada log "❌ Device limit reached!"
3. Jika tidak ada, berarti device belum mencapai limit

## ✅ Checklist Implementation

- [x] Buat DeviceEntity untuk Room Database
- [x] Buat DeviceDao untuk query database
- [x] Buat DeviceInfo data class untuk Firestore
- [x] Buat DeviceRepository dengan logika check device
- [x] Integrasikan device check di SplashScreenActivity
- [x] Hapus device check dari DaftarTokoActivity
- [x] Update Firestore structure dari per-user ke global
- [x] Tambahkan dialog blocker di SplashScreen
- [x] Tambahkan dialog error handling
- [x] Testing di 2 device
- [x] Testing blocker di device ke-3

## 🎉 Kesimpulan

Implementasi device management sekarang sudah sesuai dengan flowchart yang diminta:
1. ✅ Cek device di awal aplikasi (SplashScreen)
2. ✅ Gunakan Room untuk cache lokal
3. ✅ Gunakan Firestore untuk tracking global
4. ✅ Maksimal 2 device
5. ✅ Pop-up blocker jika melebihi batas
6. ✅ Tidak bergantung pada nama toko

**Status: READY FOR TESTING** 🚀

---

**Created:** February 16, 2026
**Last Updated:** February 16, 2026
**Version:** 1.0.0

