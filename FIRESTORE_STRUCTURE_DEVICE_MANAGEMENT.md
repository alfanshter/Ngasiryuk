# 📊 Firestore Structure - Device Management

## 🎯 Struktur Baru (Global per Aplikasi)

```
📁 Firestore Database
│
└─📂 app_devices (collection)
   │
   └─📄 com.example.ngasiryuk (document - package name)
      │
      └─📂 devices (sub-collection)
         │
         ├─📄 7f1ffc1ca5a2ea34 (document - Device ID 1)
         │  ├─ deviceId: "7f1ffc1ca5a2ea34"
         │  ├─ deviceName: "Samsung SM-A205F"
         │  ├─ registeredAt: 1739654400000
         │  └─ lastAccess: 1739654400000
         │
         └─📄 abc123def456 (document - Device ID 2)
            ├─ deviceId: "abc123def456"
            ├─ deviceName: "Xiaomi Redmi Note 10"
            ├─ registeredAt: 1739654500000
            └─ lastAccess: 1739654500000
```

## ❌ Struktur Lama (SALAH - Per User/Toko)

```
📁 Firestore Database
│
└─📂 users (collection)
   │
   ├─📄 gallon (document - nama toko 1)
   │  └─📂 devices
   │     └─📄 device1
   │
   └─📄 jaajajaja (document - nama toko 2)
      └─📂 devices
         └─📄 device1
```

**Masalah dengan struktur lama:**
- ❌ Device dicek SETELAH toko terdaftar
- ❌ Nama toko berbeda = collection berbeda
- ❌ 2 HP bisa daftar dengan nama toko berbeda
- ❌ Tidak sesuai flowchart

## ✅ Keuntungan Struktur Baru

1. **Global Tracking**
   - Semua device dicek di 1 tempat
   - Tidak peduli nama toko
   
2. **Early Detection**
   - Device dicek saat buka aplikasi
   - Sebelum user masuk ke menu apapun

3. **Konsisten**
   - 1 aplikasi = maksimal 2 device
   - Tidak bisa bypass dengan ganti nama toko

## 📍 Path Firestore

### Read Device Count
```kotlin
firestore.collection("app_devices")
    .document("com.example.ngasiryuk")
    .collection("devices")
    .get()
```

### Check Single Device
```kotlin
firestore.collection("app_devices")
    .document("com.example.ngasiryuk")
    .collection("devices")
    .document(deviceId)
    .get()
```

### Register New Device
```kotlin
firestore.collection("app_devices")
    .document("com.example.ngasiryuk")
    .collection("devices")
    .document(deviceId)
    .set(deviceInfo)
```

## 🔐 Firestore Rules

### Development (Testing)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow all for testing
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

### Production (Recommended)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Device management collection
    match /app_devices/{appId}/devices/{deviceId} {
      // Allow read untuk semua
      allow read: if true;
      
      // Allow write hanya untuk device baru atau update device sendiri
      allow create: if request.resource.data.deviceId == deviceId;
      allow update: if resource.data.deviceId == deviceId;
      
      // Tidak bisa delete
      allow delete: if false;
    }
  }
}
```

## 🧪 Testing di Firestore Console

### 1. Lihat Semua Device Terdaftar
1. Buka: https://console.firebase.google.com
2. Pilih project: **Ngasiryuk**
3. Firestore Database → **Data tab**
4. Navigate: `app_devices` → `com.example.ngasiryuk` → `devices`
5. Akan muncul list semua device

### 2. Hapus Device (untuk testing ulang)
1. Klik device yang mau dihapus
2. Klik icon **trash** (🗑️)
3. Confirm delete

### 3. Reset Semua (untuk testing dari awal)
1. Klik collection `app_devices`
2. Klik icon **trash** (🗑️)
3. Confirm "Delete collection and all its sub-collections"
4. Uninstall aplikasi dari semua HP
5. Install ulang untuk test dari awal

## 📊 Monitoring Dashboard

### Query untuk Analytics
```javascript
// Total device terdaftar
db.collection('app_devices')
  .doc('com.example.ngasiryuk')
  .collection('devices')
  .get()
  .then(snap => console.log('Total devices:', snap.size));

// Device yang aktif hari ini
const today = new Date().setHours(0,0,0,0);
db.collection('app_devices')
  .doc('com.example.ngasiryuk')
  .collection('devices')
  .where('lastAccess', '>=', today)
  .get()
  .then(snap => console.log('Active today:', snap.size));
```

## 🔄 Migration dari Struktur Lama

Jika sudah ada data di struktur lama (`users/{userId}/devices`):

### Step 1: Export Data Lama
```javascript
// Jalankan di Console Firestore
db.collectionGroup('devices').get().then(snap => {
  snap.forEach(doc => {
    console.log(doc.data());
  });
});
```

### Step 2: Hapus Data Lama
1. Hapus collection `users` di Firestore Console
2. Atau biarkan saja (tidak akan dipakai lagi)

### Step 3: Install Ulang
1. Uninstall aplikasi dari semua device
2. Install versi baru
3. Device akan otomatis register di struktur baru

## ✅ Checklist

- [x] Collection `app_devices` created
- [x] Document `com.example.ngasiryuk` auto-created
- [x] Sub-collection `devices` auto-created
- [x] Device auto-register on first open
- [x] Limit check working
- [x] Pop-up blocker working

---

**Current Structure:** Global per Application ✅  
**Old Structure:** Per User/Store ❌  
**Migration Needed:** No (auto-create on first use)

