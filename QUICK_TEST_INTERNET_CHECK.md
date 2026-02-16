# 🧪 Quick Test Guide - Internet Check for Daftar Toko

## ⚡ Test Scenarios

### ✅ Test 1: First Time User - No Internet (HARUS BLOCK)

**Steps:**
1. Uninstall aplikasi dari HP
2. **Matikan Wi-Fi dan Data Seluler**
3. Install aplikasi
4. Buka aplikasi

**Expected Result:**
```
📱 Splash Screen muncul
    ↓
🔍 Device check... ✅ Allowed
    ↓
🏪 Cek toko... Tidak ada toko
    ↓
📶 Cek internet... ❌ Tidak ada internet
    ↓
⚠️ POP-UP MUNCUL:

    📶 Koneksi Internet Diperlukan
    
    Untuk mendaftarkan toko pertama kali, aplikasi 
    memerlukan koneksi internet.
    
    Silakan nyalakan Wi-Fi atau data seluler Anda, 
    lalu coba lagi.
    
    Note: Setelah toko terdaftar, aplikasi dapat 
    digunakan secara offline.
    
    [Coba Lagi]  [Tutup Aplikasi]
```

**Action:**
- Klik "Tutup Aplikasi" → Aplikasi tertutup ✅
- Atau nyalakan internet → Klik "Coba Lagi" → Masuk ke Daftar Toko ✅

---

### ✅ Test 2: First Time User - With Internet (HARUS BISA MASUK)

**Steps:**
1. Uninstall aplikasi
2. **Nyalakan Wi-Fi atau Data**
3. Install aplikasi
4. Buka aplikasi

**Expected Result:**
```
📱 Splash Screen
    ↓
🔍 Device check... ✅ Allowed
    ↓
🏪 Cek toko... Tidak ada
    ↓
📶 Cek internet... ✅ Ada internet
    ↓
🎉 MASUK KE HALAMAN DAFTAR TOKO
```

**Verify:**
- Form daftar toko muncul
- Bisa input nama & alamat toko
- Bisa save toko

---

### ✅ Test 3: Already Registered - No Internet (OFFLINE MODE)

**Steps:**
1. Pastikan toko sudah pernah didaftarkan sebelumnya
2. **Matikan Wi-Fi dan Data Seluler**
3. Tutup aplikasi (force stop)
4. Buka aplikasi lagi

**Expected Result:**
```
📱 Splash Screen
    ↓
🔍 Device check... ✅ Allowed (dari Room DB lokal)
    ↓
🏪 Cek toko... ✅ Sudah ada
    ↓
📶 SKIP internet check (tidak perlu!)
    ↓
🚀 LANGSUNG MASUK DASHBOARD (OFFLINE MODE)
```

**Verify:**
- Dashboard langsung muncul
- **TIDAK ADA pop-up** internet diperlukan
- Semua fitur berfungsi offline (kasir, produk, dll)

---

### ✅ Test 4: Already Registered - With Internet

**Steps:**
1. Toko sudah terdaftar
2. Internet aktif
3. Buka aplikasi

**Expected Result:**
```
📱 Splash Screen
    ↓
🔍 Device check... ✅ Allowed
    ↓
🏪 Cek toko... ✅ Sudah ada
    ↓
🚀 LANGSUNG MASUK DASHBOARD
```

**Verify:**
- Dashboard muncul langsung
- Tidak ada delay karena cek internet

---

## 📊 Logcat Filter

Untuk debug, gunakan filter: `SplashScreenActivity`

### Log untuk Test 1 (First Time - No Internet):
```
D/SplashScreenActivity: 🚀 Starting device check...
D/SplashScreenActivity: ✅ Device allowed, proceeding to app
D/SplashScreenActivity: 📝 No store, need to register (checking internet...)
E/SplashScreenActivity: ❌ No internet connection for registration
```

### Log untuk Test 2 (First Time - With Internet):
```
D/SplashScreenActivity: 🚀 Starting device check...
D/SplashScreenActivity: ✅ Device allowed, proceeding to app
D/SplashScreenActivity: 📝 No store, need to register (checking internet...)
D/SplashScreenActivity: ✅ Internet available, going to register store
```

### Log untuk Test 3 & 4 (Already Registered):
```
D/SplashScreenActivity: 🚀 Starting device check...
D/SplashScreenActivity: ✅ Device allowed, proceeding to app
D/SplashScreenActivity: 🏪 Store exists, going to dashboard (offline mode)
```

---

## 🎯 Success Criteria

| Test Case | Kondisi | Expected | Pass? |
|-----------|---------|----------|-------|
| Test 1 | First time + No Internet | Pop-up muncul, block user | ☐ |
| Test 2 | First time + With Internet | Masuk Daftar Toko | ☐ |
| Test 3 | Registered + No Internet | Masuk Dashboard (offline) | ☐ |
| Test 4 | Registered + With Internet | Masuk Dashboard | ☐ |

---

## 🐛 Common Issues

### Issue: Pop-up tidak muncul di Test 1
**Check:**
- Apakah internet benar-benar mati?
- Coba test: Buka browser, pastikan tidak bisa buka web
- Cek log: Harus ada "❌ No internet connection"

### Issue: Tidak bisa masuk padahal internet aktif
**Check:**
- Cek log: Ada "✅ Internet available"?
- Pastikan Firestore rules allow access
- Test connection: Ping google.com

### Issue: Test 3 tetap minta internet
**Check:**
- Apakah toko benar-benar sudah tersimpan?
- Cek database Room: `settings.db` → tabel `toko`
- Log harus: "🏪 Store exists"

---

## ✅ Quick Checklist

Sebelum testing, pastikan:
- [ ] Aplikasi ter-build dengan perubahan terbaru
- [ ] Permission `ACCESS_NETWORK_STATE` ada di Manifest
- [ ] Firestore rules allow read/write
- [ ] Logcat siap untuk monitoring
- [ ] 2 HP (atau emulator + HP) untuk testing

---

**Tips:** Test 1 dan Test 3 adalah yang paling penting! Ini membuktikan sistem offline mode bekerja dengan baik.

**Status: READY FOR TESTING** 🚀

