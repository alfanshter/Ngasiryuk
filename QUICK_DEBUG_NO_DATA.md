# 🔍 QUICK DEBUG GUIDE - Data Tidak Masuk Firestore

## Problem
Data device tidak masuk ke Firestore saat daftar toko.

## Root Cause Analysis

Ada 3 kemungkinan:
1. **Firestore Rules memblokir write** ❌
2. **Device check tidak dipanggil** ❌
3. **Firebase belum initialized** ❌

## Solution Steps (URUTAN PENTING!)

### Step 1: Set Firestore Rules ⚡ PALING PENTING!

**Buka:** https://console.firebase.google.com/project/ngasiryuk/firestore/rules

**Set rules ini:**
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

**Klik: Publish**

### Step 2: Clear App Data
```
Settings → Apps → Ngasiryuk → Clear Data
```

### Step 3: Rebuild & Install
1. Android Studio → Build → Clean Project
2. Build → Rebuild Project
3. Run App

### Step 4: Check Logcat
Filter: `DeviceRepository|DaftarTokoViewModel|MainActivity`

**Expected logs saat app start:**
```
✅ Firebase initialized successfully
```

### Step 5: Daftar Toko
1. Isi nama toko: "Test Toko"
2. Isi alamat: "Jalan Test"
3. Klik "Simpan Profil"

### Step 6: Check Logs After Save

**Expected logs:**
```
🚀 Starting device access check...
🔍 Checking device access for: abc123...
📱 Device not in local DB, checking Firestore...
🏪 Store found. User ID: test_toko
🔥 Checking Firestore at: users/test_toko/devices/abc123...
🆕 Device not registered yet. Checking device count...
📊 Current device count: 0 / 2
📝 Registering new device...
✅ Device registered in Firestore
✅ Device saved to local Room DB
✅ Device access ALLOWED
```

### Step 7: Verify di Firestore Console

**Buka:** https://console.firebase.google.com/project/ngasiryuk/firestore/data

**Should see:**
```
📁 users
  └─ 📄 test_toko
      └─ 📁 devices
          └─ 📄 abc123456...
              • deviceId: "abc123456..."
              • deviceName: "Google Pixel 5"
              • registeredAt: 1708012345678
              • lastAccess: 1708012345678
```

## Common Errors & Fixes

### ❌ Error: PERMISSION_DENIED

**Cause:** Firestore rules masih default (deny all)

**Fix:** 
```javascript
// Di Firestore Rules, ubah jadi:
match /{document=**} {
  allow read, write: if true;  // <-- Ini!
}
```

### ❌ Error: Firebase app not initialized

**Cause:** `FirebaseApp.initializeApp()` tidak dipanggil

**Fix:** Sudah fixed di MainActivity.kt - rebuild app

### ❌ Log: "⚠️ No store registered yet"

**Cause:** Belum ada toko di database

**Fix:** Daftar toko dulu

### ❌ Log tidak muncul sama sekali

**Cause:** Logcat filter salah atau app tidak debug mode

**Fix:**
1. Pilih device di Logcat dropdown
2. Set filter ke "No Filters"
3. Search: "DeviceRepository"

### ❌ Data masih tidak masuk

**Cause:** Multiple issues

**Fix:**
1. Check internet connection
2. Verify `google-services.json` ada di `app/`
3. Check Firebase project match dengan google-services.json
4. Rebuild dari clean

## Manual Test Firestore

Jika masih tidak jalan, test manual:

### Test 1: Firestore Write Test

Tambahkan button test di DaftarToko screen:

```kotlin
Button(onClick = { viewModel.testFirestoreConnection() }) {
    Text("Test Firestore")
}
```

Klik button dan check log:
```
🧪 Testing Firestore connection...
✅ Firestore connection test SUCCESS
```

### Test 2: Check di Firestore Console

Setelah test, check di Firestore:
```
📁 test
  └─ 📄 connection_test
      • test: "connection_test"
      • timestamp: 1708012345678
      • deviceId: "abc123..."
```

## Verification Checklist

Centang semua sebelum declare "SOLVED":

- [ ] ✅ Firebase initialized successfully (check log)
- [ ] ✅ Firestore rules = `allow read, write: if true`
- [ ] ✅ Rules sudah di-publish
- [ ] ✅ App di-clear data & rebuild
- [ ] ✅ Toko sudah didaftarkan
- [ ] ✅ Log menunjukkan "Device registered in Firestore"
- [ ] ✅ Log menunjukkan "Device saved to local Room DB"
- [ ] ✅ Data muncul di Firestore console
- [ ] ✅ Collection structure: users/{nama_toko}/devices/{device_id}

## Final Verification

### Check Data Structure di Firestore:

**Correct:**
```
users/test_toko/devices/abc123
```

**Wrong:**
```
devices/abc123  (missing user hierarchy)
test_toko/abc123  (wrong structure)
```

## If Still Not Working

1. **Take screenshot** of:
   - Logcat output
   - Firestore console (empty)
   - Firestore rules

2. **Check:**
   - Package name di google-services.json = com.example.ngasiryuk
   - Firebase project = ngasiryuk
   - Internet aktif di device/emulator

3. **Last resort:**
   - Delete & recreate Firebase project
   - Generate new google-services.json
   - Rebuild app

---

**Expected Result:** 
Data muncul di Firestore dalam 1-2 detik setelah save toko.

**If successful, you'll see:**
- ✅ Log: "Device registered in Firestore"
- ✅ Data visible di Firestore console
- ✅ App tidak crash
- ✅ Dialog device block TIDAK muncul (karena device pertama)

