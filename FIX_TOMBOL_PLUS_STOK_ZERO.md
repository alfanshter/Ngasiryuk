# 🔧 Fix: Tombol Tambah Jumlah untuk Produk Stok = 0

## 🐛 Masalah
Ketika produk memiliki stok = 0, tombol **Plus (+)** tidak bisa menambah jumlah satuan di keranjang.

Ini menjadi masalah karena:
- Produk baru dari kasir otomatis punya stok = 0
- Produk existing yang stoknya belum diinput juga stok = 0
- Kasir tetap harus bisa jual produk meski stok belum diupdate

---

## ✅ Solusi

### Logika Baru Tombol Plus (+):

```kotlin
if (selectedProduk != null) {
    // Jika produk sudah ada di database
    if (selectedProduk!!.stok > 0) {
        // Produk punya stok, batasi sesuai stok
        if (jumlah < selectedProduk!!.stok) jumlah++
    } else {
        // Stok = 0, bebas tambah (produk baru dari kasir)
        jumlah++
    }
} else {
    // Produk baru, bebas tambah jumlah
    jumlah++
}
```

---

## 🎯 Penjelasan Logika

### Skenario 1: Produk Baru (selectedProduk = null)
```
✅ Bebas tambah jumlah
✅ Tidak ada batasan
```

### Skenario 2: Produk Existing dengan Stok > 0
```
✅ Batasi sesuai stok yang tersedia
❌ Tidak bisa tambah jika jumlah = stok
Contoh: Stok = 10, maksimal jumlah = 10
```

### Skenario 3: Produk Existing dengan Stok = 0
```
✅ Bebas tambah jumlah
✅ Tidak ada batasan
💡 Ini produk yang ditambahkan dari kasir sebelumnya
💡 Atau produk yang stoknya belum diinput di Manajemen Stok
```

---

## 🎨 Visual Feedback untuk User

### Produk dengan Stok > 0:
```
┌─────────────────────────────────────┐
│ ✅ Produk ditemukan - Stok: 50      │
└─────────────────────────────────────┘
Warna: Hijau (background #E8F5E9)
```

### Produk dengan Stok = 0:
```
┌──────────────────────────────────────┐
│ ⚠️ Produk ditemukan - Stok belum    │
│    diinput                           │
│    Tetap bisa dijual, update stok di │
│    Manajemen Stok                    │
└──────────────────────────────────────┘
Warna: Orange (background #FFF3E0)
```

---

## 📊 Tabel Perbandingan

| Kondisi Produk | Stok | Tombol + | Batasan |
|----------------|------|----------|---------|
| **Produk Baru** | - | ✅ Aktif | Tidak ada |
| **Existing (Stok > 0)** | 50 | ✅ Aktif | Max = 50 |
| **Existing (Stok = 0)** | 0 | ✅ Aktif | Tidak ada |

---

## 🧪 Test Cases

### Test 1: Produk Baru
```
1. Input nama produk baru "Kopi Latte"
2. Input harga jual: 18000
3. Klik tombol + berkali-kali
✅ Jumlah terus bertambah (1, 2, 3, 4...)
✅ Tidak ada batasan
```

### Test 2: Produk Existing (Stok = 20)
```
1. Pilih produk "Air Mineral" (stok = 20)
2. Klik tombol + berkali-kali
✅ Jumlah bertambah sampai 20
❌ Setelah 20, tombol + tidak bisa tambah lagi
✅ Card info: "Produk ditemukan - Stok: 20" (hijau)
```

### Test 3: Produk Existing (Stok = 0)
```
1. Pilih produk "Es Teh" (stok = 0)
2. Klik tombol + berkali-kali
✅ Jumlah terus bertambah (1, 2, 3, 4...)
✅ Tidak ada batasan
✅ Card info: "Stok belum diinput" (orange)
```

### Test 4: Produk dari Kasir Sebelumnya (Stok = 0)
```
1. Produk "Jus Alpukat" ditambahkan kemarin dari kasir
2. Hari ini pilih produk tersebut
3. Lihat card info: Orange (stok = 0)
4. Klik tombol + berkali-kali
✅ Jumlah terus bertambah
✅ Tetap bisa dijual meski stok belum diupdate
```

---

## 🔄 Alur Update Stok

### Step 1: Jual Produk Stok = 0 di Kasir
```
Produk: Kopi Susu (Stok = 0)
Jumlah: 5 pcs
Harga: Rp 15,000
✅ Transaksi berhasil
```

### Step 2: Update Stok di Manajemen Stok
```
1. Buka Menu Manajemen Stok
2. Cari "Kopi Susu"
3. Edit Produk
4. Input:
   - Stok: 100
   - Harga Beli: 10,000
5. Simpan
✅ Stok terupdate
```

### Step 3: Jual Lagi di Kasir
```
Produk: Kopi Susu (Stok = 100)
Jumlah: Maksimal 100 pcs
✅ Card info: Hijau (stok tersedia)
✅ Tombol + dibatasi sampai 100
```

---

## 💡 Keuntungan Sistem Ini

### Untuk Kasir:
- ✅ Tidak terblokir saat stok = 0
- ✅ Tetap bisa jual produk baru
- ✅ Transaksi lebih lancar
- ✅ Tidak perlu tunggu input stok dulu

### Untuk Admin/Owner:
- ✅ Bisa update stok kapan saja di Manajemen Stok
- ✅ Kasir tetap produktif
- ✅ Data stok tetap terkontrol untuk produk yang sudah diinput

### Untuk Sistem:
- ✅ Fleksibel: bisa jual dengan/tanpa stok
- ✅ Proteksi: produk dengan stok tetap dibatasi
- ✅ User-friendly: visual feedback jelas

---

## 📝 Catatan Penting

### ⚠️ Produk Stok = 0 Bisa Dijual Unlimited
- Ini **INTENTIONAL** (disengaja)
- Tujuan: kasir tidak terblokir
- Kontrol stok dilakukan di Manajemen Stok

### ✅ Produk Stok > 0 Dibatasi
- Ini **PROTEKSI** agar tidak overselling
- Jika stok = 10, tidak bisa jual 11

### 💡 Best Practice
```
1. Tambah produk baru di kasir (stok = 0)
2. Jual sesuai kebutuhan
3. Secara berkala update stok di Manajemen Stok
4. Setelah stok diinput, sistem otomatis batasi jumlah
```

---

## 🎯 Summary Perubahan

### File: `TambahKeranjangBottomSheet.kt`

#### 1. Update Logika Plus Button
```kotlin
// SEBELUM (bermasalah):
if (jumlah < selectedProduk!!.stok) jumlah++

// SESUDAH (fixed):
if (selectedProduk!!.stok > 0) {
    if (jumlah < selectedProduk!!.stok) jumlah++
} else {
    jumlah++ // Bebas jika stok = 0
}
```

#### 2. Update Visual Card Info
- Warna hijau: Stok > 0
- Warna orange: Stok = 0
- Info tambahan untuk stok = 0

---

## ✅ Testing Checklist

- [x] Produk baru: tombol + tidak dibatasi
- [x] Produk stok > 0: tombol + dibatasi sesuai stok
- [x] Produk stok = 0: tombol + tidak dibatasi
- [x] Visual card: hijau untuk stok > 0
- [x] Visual card: orange untuk stok = 0
- [x] Info text sesuai kondisi stok
- [x] No errors saat compile

---

## 🚀 Status
✅ **Fix completed**
✅ **Tested**
✅ **Ready to use**

**Tanggal:** 17 Februari 2026
**File:** TambahKeranjangBottomSheet.kt
**Masalah:** Tombol + tidak bisa tambah jumlah untuk stok = 0
**Solusi:** Tambahkan kondisi khusus untuk stok = 0 (bebas tambah)

