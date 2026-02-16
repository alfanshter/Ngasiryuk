# FIRESTORE RULES SETUP

## ⚠️ PENTING: Set Rules untuk Testing

Firestore saat ini kosong karena **rules default memblokir semua write operation**.

## Langkah Setup:

### 1. Buka Firebase Console
URL: https://console.firebase.google.com/project/ngasiryuk/firestore

### 2. Pilih Tab "Rules"
Di Firestore Database → Klik tab **"Rules"**

### 3. Copy & Paste Rules Berikut:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow all read/write for TESTING ONLY
    // ⚠️ JANGAN gunakan di production!
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

### 4. Klik "Publish"

### 5. Verify Rules Active
Setelah publish, rules akan tampil seperti ini:
```
match /{document=**} {
  allow read, write: if true;
}
```

## ⚠️ Security Warning

**Rules ini TIDAK AMAN untuk production!** 

Rules ini hanya untuk testing. Setelah aplikasi jalan, ubah rules menjadi:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Rules untuk production
    match /users/{userId}/devices/{deviceId} {
      // Hanya allow read/write untuk authenticated users
      allow read, write: if request.auth != null;
    }
    
    // Default deny all
    match /{document=**} {
      allow read, write: if false;
    }
  }
}
```

## Cara Test Setelah Rules Diubah

### 1. Buka Logcat di Android Studio
Filter: `DeviceRepository` atau `DaftarTokoViewModel`

### 2. Jalankan Aplikasi

### 3. Check Log Output
Anda harus melihat:
```
✅ Firebase initialized successfully
🔍 Checking device access for: abc123...
📱 Device not in local DB, checking Firestore...
⚠️ No store registered yet
```

### 4. Daftar Toko
Isi nama toko dan alamat, lalu save.

### 5. Check Log Lagi
Setelah save toko, check device access akan jalan lagi:
```
🏪 Store found. User ID: nama_toko
🔥 Checking Firestore at: users/nama_toko/devices/abc123
🆕 Device not registered yet. Checking device count...
📊 Current device count: 0 / 2
📝 Registering new device...
✅ Device registered in Firestore
✅ Device saved to local Room DB
```

### 6. Verify di Firestore Console
Buka: https://console.firebase.google.com/project/ngasiryuk/firestore/data

Anda harus melihat:
```
users (collection)
  └─ nama_toko (document)
      └─ devices (collection)
          └─ abc123... (document)
              ├─ deviceId: "abc123..."
              ├─ deviceName: "Samsung Galaxy..."
              ├─ registeredAt: 1234567890
              └─ lastAccess: 1234567890
```

## Troubleshooting

### Error: PERMISSION_DENIED
**Solusi:** Rules belum diubah atau belum publish. Set rules ke `allow read, write: if true;`

### Error: Firebase not initialized
**Solusi:** Pastikan `FirebaseApp.initializeApp(this)` dipanggil di MainActivity

### Tidak ada log muncul
**Solusi:** 
1. Check Logcat filter
2. Pastikan aplikasi running di debug mode
3. Check "No Filters" di Logcat dropdown

### Data tetap tidak masuk
**Solusi:**
1. Clear app data
2. Uninstall & reinstall app
3. Check internet connection
4. Verify google-services.json ada di app/

## Status Check

- [ ] Firestore rules sudah diubah
- [ ] Rules sudah di-publish
- [ ] Aplikasi sudah di-rebuild
- [ ] Log menunjukkan "Firebase initialized successfully"
- [ ] Toko sudah didaftarkan
- [ ] Device check dipanggil setelah save toko
- [ ] Data muncul di Firestore console

---

**Next:** Jalankan aplikasi dan check Logcat untuk melihat proses detail!

