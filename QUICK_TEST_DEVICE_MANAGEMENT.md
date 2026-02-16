# 🚀 Quick Start - Device Management Test Guide

## ✅ Implementasi Selesai!

Sistem device management sudah berhasil diimplementasikan sesuai flowchart yang Anda minta.

## 🔄 Alur Baru

```
📱 Buka Aplikasi
    ↓
🔍 Cek Device di Room
    ↓ (tidak ada)
🔥 Cek Device di Firestore
    ↓ (tidak ada)
📊 Cek Jumlah Device
    ├─ < 2 device → ✅ Masuk aplikasi
    └─ ≥ 2 device → ❌ Pop-up blocker
```

## 🎯 Perubahan Utama

### 1. Cek Device di SplashScreen (BUKAN di Daftar Toko)
- Sekarang device dicek **langsung saat aplikasi dibuka**
- Jika terblokir, aplikasi langsung ditutup
- User tidak bisa masuk sama sekali jika sudah 2 device terdaftar

### 2. Firestore Structure Baru
**SEBELUM** (salah): 
```
users/{namaToko}/devices/{deviceId}  ❌
```

**SEKARANG** (benar):
```
app_devices/com.example.ngasiryuk/devices/{deviceId}  ✅
```

**Kenapa?** Karena device harus dicek SEBELUM toko terdaftar!

### 3. Tidak Bergantung Nama Toko
- Device dicek berdasarkan **package name aplikasi**
- Tidak peduli nama tokonya apa
- 1 aplikasi = maksimal 2 device, **TITIK!**

## 🧪 Cara Test

### Test 1: Device Pertama ✅
1. Uninstall aplikasi dari semua device
2. **Hapus data di Firestore Console:**
   - Buka: https://console.firebase.google.com
   - Pilih project: Ngasiryuk
   - Firestore Database → `app_devices` → **Delete Collection**
3. Install di HP 1
4. Buka aplikasi
5. **Expected:** Langsung masuk, daftar toko kalau belum ada
6. **Cek log:** 
   ```
   ✅ Device registered in Firestore
   ✅ Device saved to local Room DB
   ✅ Device access ALLOWED
   ```

### Test 2: Device Kedua ✅
1. Install di HP 2 (tanpa hapus HP 1)
2. Buka aplikasi
3. **Expected:** Langsung masuk
4. **Cek Firestore Console:** Harus ada 2 device

### Test 3: Device Ketiga (BLOCKER) ❌
1. Install di HP 3
2. Buka aplikasi
3. **Expected:** Muncul pop-up:
   ```
   ⚠️ Batas Device Tercapai
   
   Aplikasi ini sudah digunakan di 2 perangkat.
   
   Batas maksimal penggunaan adalah 2 perangkat.
   
   [Tutup Aplikasi]
   ```
4. Klik "Tutup Aplikasi"
5. Aplikasi tertutup
6. **Cek log:**
   ```
   ❌ Device limit reached! (2 devices already registered)
   ```

## 📝 Mengubah Limit Device

Edit: `DeviceRepository.kt` line 22
```kotlin
private val maxDevices = 2  // Ubah jadi 1, 3, atau 5
```

## 🔥 Firestore Rules (Penting!)

Pastikan rules mengizinkan akses:
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

## 📊 Monitoring di Firestore

Path: `app_devices/com.example.ngasiryuk/devices/`

Setiap device akan punya data:
- **deviceId**: Android ID unik
- **deviceName**: Model HP (misal: "Samsung A20")
- **registeredAt**: Timestamp registrasi
- **lastAccess**: Timestamp terakhir akses

## 🐛 Troubleshooting

### Pop-up tidak muncul?
- Cek Logcat filter: `SplashScreenActivity`
- Pastikan ada log: `❌ Device limit reached!`

### Device count selalu 0?
- Cek internet connection
- Cek Firestore rules (harus allow read/write)
- Cek Firebase sudah terkoneksi

### Firestore permission denied?
- Buka Firebase Console
- Firestore Database → Rules
- Pastikan: `allow read, write: if true;`

## ✅ Status

- [x] Device check di SplashScreen
- [x] Firestore global structure
- [x] Pop-up blocker
- [x] Room database cache
- [x] Error handling
- [x] Dialog retry mechanism

**READY FOR TESTING!** 🎉

---

**Test Firestore Connection:**
Buka aplikasi → Lihat log → Cari: "✅ Firebase initialized successfully"

