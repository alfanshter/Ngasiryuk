# QUICK TEST GUIDE - DEVICE MANAGEMENT

## Persiapan

### 1. Firebase Setup
1. Buka Firebase Console: https://console.firebase.google.com/
2. Pastikan `google-services.json` sudah ada di folder `app/`
3. Enable Firestore Database
4. Set Firestore Rules (untuk testing):
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

### 2. Build Project
1. Sync Gradle
2. Clean & Rebuild Project
3. Install aplikasi ke device/emulator

## Test Scenarios

### Scenario 1: First Device Registration (With Internet)

**Steps:**
1. Install aplikasi di device pertama (atau clear data jika sudah install)
2. Pastikan internet AKTIF
3. Buka aplikasi
4. **Expected Result:**
   - Tidak ada dialog internet
   - Tidak ada dialog block
   - Langsung masuk ke halaman Daftar Toko
5. Isi form Daftar Toko:
   - Nama Toko: "Test Toko 1"
   - Alamat: "Jl. Test No. 1"
6. Klik "Simpan Profil"
7. **Verify di Firestore:**
   - Collection: `users/test_toko_1/devices`
   - Should have 1 document dengan deviceId

### Scenario 2: Second Device Registration

**Steps:**
1. Install aplikasi di device kedua (atau emulator lain)
2. Buka aplikasi
3. **Expected Result:**
   - Tidak ada dialog block
   - Masuk ke halaman Daftar Toko
4. Isi form dengan NAMA TOKO YANG SAMA: "Test Toko 1"
5. Klik "Simpan Profil"
6. **Verify di Firestore:**
   - Collection: `users/test_toko_1/devices`
   - Should have 2 documents

### Scenario 3: Third Device (Should Be Blocked)

**Steps:**
1. Install aplikasi di device ketiga
2. Buka aplikasi
3. **Expected Result:**
   - Dialog "Akses Ditolak" muncul
   - Message: "Aplikasi sudah digunakan di 2 perangkat. Maksimal penggunaan adalah 2 perangkat."
   - Tombol "Tutup Aplikasi"
4. Klik "Tutup Aplikasi"
5. **Expected:** Aplikasi tertutup

### Scenario 4: No Internet Connection

**Steps:**
1. Clear app data atau install di device baru
2. **MATIKAN** internet/wifi
3. Buka aplikasi
4. **Expected Result:**
   - Dialog "Tidak Ada Koneksi Internet" muncul
   - Message: "Untuk mendaftarkan toko, aplikasi memerlukan koneksi internet..."
   - Tombol "Coba Lagi"
5. Klik "Coba Lagi" (tanpa nyalakan internet dulu)
6. **Expected:** Dialog masih muncul (karena masih belum ada internet)
7. Nyalakan internet
8. Klik "Coba Lagi" lagi
9. **Expected:** Dialog hilang, proceed ke device check

### Scenario 5: Returning User (Offline Mode)

**Steps:**
1. Gunakan device yang sudah pernah login (ada di Room Database)
2. **MATIKAN** internet
3. Buka aplikasi
4. **Expected Result:**
   - Tidak ada dialog internet
   - Tidak ada dialog block
   - Langsung masuk ke aplikasi (menggunakan data dari Room)

### Scenario 6: Edit Toko (Existing Device)

**Steps:**
1. Gunakan device yang sudah terdaftar
2. Buka aplikasi (dengan internet)
3. Dari Dashboard, masuk ke menu "Daftar Toko"
4. **Expected Result:**
   - Form sudah terisi dengan data toko sebelumnya
   - Tidak ada dialog block
5. Edit data toko
6. Klik "Update Profil"
7. **Expected:** Data ter-update

## Debugging

### Check Room Database
Gunakan Database Inspector di Android Studio:
1. View → Tool Windows → App Inspection
2. Pilih tab "Database Inspector"
3. Check table `device`

### Check Firestore
1. Buka Firebase Console
2. Go to Firestore Database
3. Navigate: users → {userId} → devices
4. Check documents

### Enable Logging
Tambahkan di DeviceRepository.kt:
```kotlin
suspend fun checkDeviceAccess(): DeviceAccessResult {
    return try {
        val deviceId = getDeviceId()
        Log.d("DeviceRepo", "Checking device: $deviceId")
        
        val localDevice = deviceDao.getDevice()
        Log.d("DeviceRepo", "Local device: ${localDevice?.deviceId}")
        
        // ... rest of code
    } catch (e: Exception) {
        Log.e("DeviceRepo", "Error: ${e.message}", e)
        DeviceAccessResult.Error(e.message ?: "Unknown error")
    }
}
```

### Check Logcat
Filter by tag: "DeviceRepo" atau "DaftarToko"

## Reset for Testing

### Clear Single Device
```
Settings → Apps → Ngasiryuk → Clear Data
```

### Remove Device from Firestore
1. Firebase Console → Firestore
2. Delete specific device document

### Reset All Devices
1. Firebase Console → Firestore
2. Delete entire user document
3. Clear data di semua device

## Common Issues

### Issue 1: Dialog tidak muncul
**Check:**
- LaunchedEffect di DaftarToko.kt
- State di ViewModel
- Log di DeviceRepository

### Issue 2: Device limit tidak work
**Check:**
- Firestore query results
- userId yang digunakan (harus sama = nama toko)
- Log jumlah devices: `registeredDevices.size()`

### Issue 3: Internet dialog terus muncul
**Check:**
- NetworkUtils.isInternetAvailable()
- Permission di Manifest
- Network state di device

### Issue 4: Firestore tidak update
**Check:**
- Firestore rules
- google-services.json
- Internet connection
- Log Firestore error

## Expected Firestore Structure

After successful registration:
```
users (collection)
  └─ test_toko_1 (document)
      └─ devices (collection)
          ├─ abc123def456 (document)
          │   ├─ deviceId: "abc123def456"
          │   ├─ deviceName: "Samsung SM-A525F"
          │   ├─ registeredAt: 1708012345678
          │   └─ lastAccess: 1708012345678
          └─ xyz789ghi012 (document)
              ├─ deviceId: "xyz789ghi012"
              ├─ deviceName: "Xiaomi M2101K6G"
              ├─ registeredAt: 1708012456789
              └─ lastAccess: 1708012456789
```

## Tips

1. **Gunakan emulator** untuk test multiple devices
2. **Screenshot Firestore** setiap step untuk debugging
3. **Check Logcat** untuk melihat flow
4. **Test offline mode** untuk memastikan Room Database work
5. **Clear data** setelah tiap test scenario

## Success Criteria

✅ Device pertama bisa register
✅ Device kedua bisa register
✅ Device ketiga di-block
✅ Dialog internet muncul saat no connection
✅ Offline mode work untuk returning user
✅ Data tersimpan di Room dan Firestore
✅ Last access ter-update setiap buka app
✅ User ID based on nama toko (lowercase)

## Next Test (Optional)

1. Test dengan 2 device real (bukan emulator)
2. Test uninstall & reinstall
3. Test clear cache vs clear data
4. Test concurrent access (2 device buka bersamaan)
5. Test Firestore offline persistence

