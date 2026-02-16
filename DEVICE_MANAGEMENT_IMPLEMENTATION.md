# IMPLEMENTASI DEVICE MANAGEMENT DENGAN FIREBASE FIRESTORE

## Overview
Sistem device management telah diimplementasikan untuk membatasi penggunaan aplikasi maksimal 2 device per toko. Sistem ini menggunakan kombinasi Room Database (lokal) dan Firebase Firestore (cloud) untuk tracking device.

## Alur Sistem

### 1. Saat Aplikasi Dibuka (Halaman Daftar Toko)
```
1. Cek koneksi internet
   └─ Jika tidak ada internet → Tampilkan dialog "Tidak Ada Koneksi Internet"
   └─ Jika ada internet → Lanjut ke step 2

2. Cek device di Room Database lokal
   └─ Jika device terdaftar → Pengguna masuk ke aplikasi
   └─ Jika belum terdaftar → Lanjut ke step 3

3. Cek device di Firebase Firestore
   └─ Jika device ditemukan di Firestore
      ├─ Simpan ke Room Database
      └─ Pengguna masuk ke aplikasi
   └─ Jika device tidak ditemukan → Lanjut ke step 4

4. Cek jumlah device yang terdaftar di Firestore
   └─ Jika < 2 device
      ├─ Daftarkan device baru ke Firestore
      ├─ Simpan ke Room Database
      └─ Pengguna masuk ke aplikasi
   └─ Jika >= 2 device
      └─ Tampilkan dialog "Akses Ditolak" dan tutup aplikasi
```

## Struktur Firebase Firestore

```
users (collection)
  └─ {userId} (document) // menggunakan nama toko yang di-lowercase
      └─ devices (sub-collection)
          ├─ {deviceId1} (document)
          │   ├─ deviceId: "abc123"
          │   ├─ deviceName: "Samsung Galaxy S21"
          │   ├─ registeredAt: 1234567890
          │   └─ lastAccess: 1234567890
          └─ {deviceId2} (document)
              ├─ deviceId: "def456"
              ├─ deviceName: "Xiaomi Redmi Note 10"
              ├─ registeredAt: 1234567891
              └─ lastAccess: 1234567891
```

## File-file yang Dibuat/Dimodifikasi

### 1. Entity & DAO (Room Database)
- **DeviceEntity.kt** - Entity untuk menyimpan data device di Room
- **DeviceDao.kt** - DAO untuk akses data device di Room
- **Migration_7_8** - Migration untuk menambahkan tabel device

### 2. Model & Repository
- **DeviceInfo.kt** - Model domain untuk device
- **DeviceRepository.kt** - Repository untuk manage device (Room + Firestore)

### 3. Utility
- **NetworkUtils.kt** - Utility untuk cek koneksi internet

### 4. ViewModel & UI
- **DaftarTokoViewModel.kt** - Ditambahkan method `checkDeviceAccess()`
- **DaftarTokoState.kt** - Ditambahkan state untuk device checking
- **DaftarToko.kt** - Ditambahkan dialog internet & device block

### 5. Configuration
- **AndroidManifest.xml** - Ditambahkan permission INTERNET dan ACCESS_NETWORK_STATE
- **AppDatabase.kt** - Ditambahkan DeviceEntity dan DeviceDao
- **Migrations.kt** - Ditambahkan MIGRATION_7_8
- **AppContainer.kt** - Ditambahkan DeviceRepository

## Fitur yang Diimplementasikan

### 1. Dialog Koneksi Internet
- Muncul saat halaman Daftar Toko dibuka dan tidak ada koneksi internet
- User tidak bisa dismiss dialog
- Tombol "Coba Lagi" untuk re-check koneksi

### 2. Dialog Device Block
- Muncul jika device melebihi batas maksimal (2 device)
- Menampilkan pesan "Aplikasi sudah digunakan di X perangkat"
- Tombol "Tutup Aplikasi" untuk keluar dari aplikasi
- User tidak bisa dismiss dialog

### 3. Auto Device Registration
- Device otomatis terdaftar saat pertama kali menggunakan aplikasi
- Data device disimpan di Room (lokal) dan Firestore (cloud)
- Update last access setiap kali aplikasi dibuka

## Cara Kerja Detail

### Device ID
Menggunakan `Settings.Secure.ANDROID_ID` sebagai unique identifier device:
```kotlin
@SuppressLint("HardwareIds")
fun getDeviceId(): String {
    return Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}
```

### User ID
Menggunakan nama toko (lowercase, tanpa spasi) sebagai userId di Firestore:
```kotlin
val userId = toko.namaToko.replace(" ", "_").lowercase()
```

### Check Device Access
```kotlin
suspend fun checkDeviceAccess(): DeviceAccessResult {
    // 1. Cek Room Database lokal
    // 2. Cek Firebase Firestore
    // 3. Validasi jumlah device
    // 4. Return result (Allowed/LimitReached/Error)
}
```

## Penggunaan

### Di DaftarTokoViewModel
```kotlin
fun checkDeviceAccess() {
    viewModelScope.launch {
        _state.update { it.copy(isCheckingDevice = true) }
        when (val result = deviceRepository.checkDeviceAccess()) {
            is DeviceAccessResult.Allowed -> {
                // Izinkan akses
            }
            is DeviceAccessResult.LimitReached -> {
                // Blokir akses
            }
            is DeviceAccessResult.Error -> {
                // Tampilkan error
            }
        }
    }
}
```

### Di DaftarToko Screen
```kotlin
LaunchedEffect(Unit) {
    if (!NetworkUtils.isInternetAvailable(context)) {
        showInternetDialog = true
    } else {
        viewModel.checkDeviceAccess()
    }
}
```

## Konfigurasi Firebase

### 1. Firebase Console
- Buat project di Firebase Console
- Tambahkan aplikasi Android dengan package name yang sesuai
- Download `google-services.json` dan letakkan di folder `app/`

### 2. Firebase Firestore Rules
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/devices/{deviceId} {
      allow read, write: if true; // Atau sesuaikan dengan auth rules Anda
    }
  }
}
```

## Testing

### Test Case 1: First Time User
1. Install aplikasi di device pertama
2. Buka aplikasi → Harus bisa masuk (device 1 terdaftar)
3. Install di device kedua
4. Buka aplikasi → Harus bisa masuk (device 2 terdaftar)
5. Install di device ketiga
6. Buka aplikasi → Harus muncul dialog "Akses Ditolak"

### Test Case 2: Returning User
1. Buka aplikasi di device yang sudah terdaftar
2. Harus langsung masuk tanpa check Firestore (dari Room)

### Test Case 3: No Internet
1. Matikan koneksi internet
2. Buka aplikasi (belum pernah login sebelumnya)
3. Harus muncul dialog "Tidak Ada Koneksi Internet"
4. Nyalakan internet → Klik "Coba Lagi"
5. Harus bisa proceed ke device check

### Test Case 4: Offline Mode
1. Sudah pernah login sebelumnya (device terdaftar di Room)
2. Matikan internet
3. Buka aplikasi
4. Harus tetap bisa masuk (menggunakan data Room)

## Maintenance

### Reset Device untuk Testing
Untuk reset device saat testing, hapus data aplikasi:
```
Settings → Apps → Ngasiryuk → Clear Data
```

### Hapus Device dari Firestore
1. Buka Firebase Console
2. Go to Firestore Database
3. Navigate to: users → {userId} → devices
4. Hapus document device yang diinginkan

### Debug Log
Tambahkan log di DeviceRepository untuk debugging:
```kotlin
Log.d("DeviceRepository", "Device ID: $deviceId")
Log.d("DeviceRepository", "Registered devices: ${registeredDevices.size()}")
```

## Catatan Penting

1. **Internet hanya diperlukan untuk Daftar Toko**, fitur lain tetap offline
2. **Device ID unik per device**, tidak akan sama meskipun aplikasi di-uninstall
3. **Room Database sebagai cache**, mengurangi akses ke Firestore
4. **Last Access tracking**, untuk monitoring aktivitas device
5. **Maximum 2 devices**, sesuai requirement

## Troubleshooting

### Error: "Unresolved reference: firebase"
- Pastikan `google-services.json` sudah ada di folder `app/`
- Sync gradle

### Error: "No internet connection"
- Cek permission INTERNET di AndroidManifest.xml
- Cek Firebase configuration

### Device tidak terdaftar
- Clear app data dan coba lagi
- Check Firestore rules

### Dialog tidak muncul
- Check state di ViewModel
- Check LaunchedEffect di DaftarToko.kt

## Next Steps

1. **Implementasi Authentication** - Gunakan Firebase Auth untuk user management
2. **Device Management UI** - Buat halaman untuk manage registered devices
3. **Remote Device Removal** - Fitur untuk remove device dari Firestore
4. **Push Notification** - Notifikasi saat ada device baru terdaftar
5. **Analytics** - Track device usage dengan Firebase Analytics

## Dependencies

Pastikan dependencies ini ada di `app/build.gradle.kts`:
```kotlin
implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
implementation("com.google.firebase:firebase-firestore-ktx")
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)
ksp(libs.androidx.room.compiler)
```

