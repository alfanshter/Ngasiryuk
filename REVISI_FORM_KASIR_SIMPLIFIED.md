# Revisi Form Tambah Produk di Kasir - Simplified

## 📋 Perubahan yang Dilakukan

### ✅ Form di Menu Kasir (TambahKeranjangBottomSheet)
Telah disederhanakan sesuai kebutuhan operasional kasir:

#### Field yang DITAMPILKAN:
1. **Nama Produk** (WAJIB)
   - Autocomplete dari database produk
   - Bisa input manual jika produk baru

2. **SKU/Barcode** (OPSIONAL)
   - Boleh diisi atau dikosongkan
   - Tombol Scan QR tersedia untuk input cepat
   - Jika kosong, sistem otomatis isi dengan "-"

3. **Harga Jual** (WAJIB)
   - Input harga jual per unit/pcs
   - Bisa disesuaikan meskipun produk sudah ada

4. **Jumlah** (WAJIB)
   - Counter +/- untuk jumlah barang
   - **Logika Tombol Plus (+):**
     - Produk baru: tidak dibatasi
     - Produk existing stok > 0: dibatasi sesuai stok
     - Produk existing stok = 0: tidak dibatasi (tetap bisa dijual)
   - **Visual Feedback:**
     - Card hijau: produk dengan stok > 0
     - Card orange: produk dengan stok = 0 (tetap bisa dijual)

#### Field yang DISEMBUNYIKAN (Hanya di Menu Manajemen Stok):
- ❌ Stok (dikelola di Manajemen Stok)
- ❌ Kategori (dikelola di Manajemen Stok)
- ❌ Harga Beli (dikelola di Manajemen Stok)

---

## 🎯 Alur Kerja

### Skenario 1: Produk Sudah Ada di Database
```
1. Kasir ketik nama produk
2. Pilih dari autocomplete
3. Harga Jual muncul otomatis (bisa disesuaikan)
4. Atur jumlah
5. Simpan → Masuk keranjang
```

### Skenario 2: Produk Baru (Belum Ada di Database)
```
1. Kasir ketik nama produk baru
2. Isi SKU/Barcode (OPSIONAL)
3. Input Harga Jual
4. Atur jumlah
5. Simpan → Produk otomatis masuk ke database dengan:
   - Stok = 0
   - Harga Beli = 0
   - Kategori = "Umum" (ID: 1)
   - SKU = input user atau "-"
```

---

## 💾 Data yang Disimpan untuk Produk Baru dari Kasir

```kotlin
ProdukEntity(
    id = 0,
    namaProduk = namaProduk,           // Input dari user
    sku = skuBarcode.ifEmpty { "-" },  // Opsional
    stok = 0,                          // Default 0
    kategoriId = 1,                    // Default kategori Umum
    kategoriNama = "Umum",             // Default
    hargaBeli = 0.0,                   // Default 0
    hargaJual = hargaJual              // Input dari user
)
```

---

## 🔄 Update Stok & Harga Beli

Produk yang ditambahkan dari kasir **tidak memiliki stok dan harga beli**.

Untuk melengkapi data:
1. Buka Menu **Manajemen Stok**
2. Edit produk yang dimaksud
3. Input:
   - Jumlah Stok
   - Harga Beli
   - Kategori (jika perlu)

### 💡 Catatan Penting: Produk Stok = 0 Tetap Bisa Dijual
- Produk dengan stok = 0 **TETAP BISA dijual** di kasir
- Tombol **Plus (+)** tidak dibatasi jika stok = 0
- Ini memungkinkan kasir tetap produktif meski stok belum diinput
- Setelah stok diupdate di Manajemen Stok, sistem otomatis membatasi jumlah sesuai stok

---

## ✨ Keuntungan Sistem Ini

### Untuk Kasir:
- ✅ Form lebih sederhana dan cepat
- ✅ Fokus pada data penting: Nama & Harga Jual
- ✅ Tidak perlu mikir stok atau harga beli
- ✅ Bisa langsung jual barang baru tanpa ribet

### Untuk Manajemen:
- ✅ Kontrol stok tetap di satu tempat
- ✅ Harga beli tidak perlu diketahui kasir
- ✅ Data lebih terorganisir
- ✅ Produk baru dari kasir mudah diidentifikasi (stok = 0)

### Untuk Sistem:
- ✅ Konsistensi data terjaga
- ✅ Validasi lebih sederhana
- ✅ Pemisahan peran jelas (Kasir vs Admin)

---

## 🎨 UI Form Kasir

```
┌─────────────────────────────────────┐
│      Tambah Produk                  │
├─────────────────────────────────────┤
│                                     │
│  Nama Produk *                      │
│  [___________________________]      │
│  📝 Autocomplete dropdown           │
│                                     │
│  ✅ Produk ditemukan (jika ada)     │
│                                     │
│  SKU/Barcode (Opsional)             │
│  [_____________________] [📷 QR]    │
│                                     │
│  Harga Jual *                       │
│  Rp [________________________]      │
│                                     │
│  Jumlah          [➖] 1 [➕]        │
│                                     │
├─────────────────────────────────────┤
│  [  Batal  ]    [  Simpan  ]        │
└─────────────────────────────────────┘
```

---

## 📝 Validasi Form

### Field Wajib:
- ✅ Nama Produk (tidak boleh kosong)
- ✅ Harga Jual (tidak boleh kosong)

### Field Opsional:
- ⚪ SKU/Barcode (boleh kosong)

### Tombol Simpan:
- Aktif jika: `namaProduk.isNotEmpty() && hargaJual.isNotEmpty()`
- Nonaktif jika: salah satu field wajib kosong

---

## 🔧 File yang Dimodifikasi

```
app/src/main/java/com/example/ngasiryuk/screen/component/bottomsheet/
└── TambahKeranjangBottomSheet.kt
```

### Perubahan Utama:
1. ✅ Menghapus state: `stok`, `kategori`, `selectedKategoriId`, `hargaBeli`, `expandedKategori`
2. ✅ Menghapus UI field: Stok, Kategori, Harga Beli
3. ✅ Update placeholder SKU: "SKU/Kode Barcode (Opsional)"
4. ✅ Simplifikasi validasi form
5. ✅ Update logika simpan produk baru dengan default value
6. ✅ Cleanup unused imports

---

## 🚀 Testing

### Test Case 1: Produk Existing
1. Buka Kasir
2. Klik "Tambah Produk"
3. Ketik nama produk yang sudah ada
4. Pilih dari dropdown
5. Verifikasi harga jual muncul
6. Ubah jumlah
7. Klik Simpan
8. ✅ Produk masuk keranjang dengan harga yang dipilih

### Test Case 2: Produk Baru Tanpa SKU
1. Buka Kasir
2. Klik "Tambah Produk"
3. Ketik nama produk baru
4. **Kosongkan SKU** (opsional)
5. Input harga jual
6. Klik Simpan
7. ✅ Produk masuk keranjang
8. ✅ Produk tersimpan di database dengan SKU = "-"

### Test Case 3: Produk Baru Dengan SKU/Scan
1. Buka Kasir
2. Klik "Tambah Produk"
3. Ketik nama produk baru
4. Klik tombol QR atau input SKU manual
5. Input harga jual
6. Klik Simpan
7. ✅ Produk tersimpan dengan SKU yang diinput

### Test Case 4: Validasi Form
1. Buka Kasir
2. Klik "Tambah Produk"
3. Kosongkan semua field
4. ✅ Tombol Simpan disabled
5. Isi hanya Nama Produk
6. ✅ Tombol Simpan tetap disabled
7. Isi Harga Jual juga
8. ✅ Tombol Simpan aktif (meskipun SKU kosong)

---

## 📊 Perbedaan: Kasir vs Manajemen Stok

| Aspek | Menu Kasir | Menu Manajemen Stok |
|-------|-----------|---------------------|
| **Nama Produk** | ✅ Wajib | ✅ Wajib |
| **SKU/Barcode** | ⚪ Opsional | ✅ Wajib |
| **Stok** | ❌ Tidak ada | ✅ Wajib |
| **Kategori** | ❌ Tidak ada (default "Umum") | ✅ Wajib |
| **Harga Beli** | ❌ Tidak ada (default 0) | ✅ Wajib |
| **Harga Jual** | ✅ Wajib | ✅ Wajib |
| **Tujuan** | Input cepat saat transaksi | Input lengkap untuk inventory |

---

## 🎯 Summary

### Apa yang Berubah:
- Form kasir lebih sederhana (hanya Nama + Harga Jual + SKU Opsional)
- Stok, Kategori, Harga Beli disembunyikan dari kasir
- Produk baru dari kasir otomatis tersimpan dengan data default

### Apa yang Tetap:
- Menu Manajemen Stok tetap lengkap (semua field)
- Autocomplete produk tetap berfungsi
- Scan QR untuk SKU tetap tersedia
- Custom harga jual tetap bisa dilakukan

### Keuntungan:
- Kasir lebih cepat input
- Data lebih terorganisir
- Pemisahan peran jelas
- Sistem lebih user-friendly

---

## ✅ Status
✅ Revisi selesai
✅ Validasi passed
✅ Siap digunakan

---

**Tanggal:** 17 Februari 2026
**File:** TambahKeranjangBottomSheet.kt
**Status:** ✅ Completed



