# 🧪 Quick Test - Form Kasir Simplified

## ⚡ Test Cepat

### Test 1: Produk Baru TANPA SKU ✅
```
1. Buka Menu Kasir
2. Tap "Tambah Produk"
3. Input:
   - Nama: "Kopi Susu"
   - SKU: (KOSONGKAN)
   - Harga Jual: 15000
4. Tap Simpan
✅ Produk masuk keranjang
✅ Produk masuk database dengan SKU = "-"
```

### Test 2: Produk Baru DENGAN SKU ✅
```
1. Buka Menu Kasir
2. Tap "Tambah Produk"
3. Input:
   - Nama: "Teh Manis"
   - SKU: "TEH001"
   - Harga Jual: 8000
4. Tap Simpan
✅ Produk masuk keranjang
✅ Produk masuk database dengan SKU = "TEH001"
```

### Test 3: Scan Barcode ✅
```
1. Buka Menu Kasir
2. Tap "Tambah Produk"
3. Tap tombol QR 📷
4. Scan barcode
5. Input:
   - Nama: "Air Mineral"
   - SKU: (terisi otomatis dari scan)
   - Harga Jual: 5000
6. Tap Simpan
✅ Produk tersimpan dengan barcode
```

### Test 4: Produk Existing ✅
```
1. Buka Menu Kasir
2. Tap "Tambah Produk"
3. Ketik nama produk yang sudah ada
4. Pilih dari autocomplete
✅ Harga Jual muncul otomatis
5. Ubah harga jika perlu
6. Atur jumlah
7. Tap Simpan
✅ Produk masuk keranjang
```

### Test 5: Validasi Form ✅
```
1. Buka Menu Kasir
2. Tap "Tambah Produk"
3. Kosongkan semua field
❌ Tombol Simpan DISABLED

4. Isi hanya Nama Produk
❌ Tombol Simpan tetap DISABLED

5. Isi Harga Jual juga
✅ Tombol Simpan AKTIF (meski SKU kosong)
```

---

## 🎯 Checklist Fitur

- ✅ SKU opsional (boleh kosong)
- ✅ Hanya perlu Nama + Harga Jual
- ✅ Stok tidak muncul di form
- ✅ Kategori tidak muncul di form
- ✅ Harga Beli tidak muncul di form
- ✅ Tombol Scan QR tetap ada
- ✅ Autocomplete produk existing
- ✅ Produk baru auto save dengan default value
- ✅ Validasi hanya Nama + Harga Jual

---

## 📊 Verifikasi Data di Database

### Cek Produk Baru dari Kasir:
```sql
SELECT * FROM produk 
WHERE stok = 0 
AND hargaBeli = 0.0
AND kategoriNama = 'Umum'
```

Hasil yang diharapkan:
```
| namaProduk | sku      | stok | kategoriNama | hargaBeli | hargaJual |
|------------|----------|------|--------------|-----------|-----------|
| Kopi Susu  | -        | 0    | Umum         | 0.0       | 15000.0   |
| Teh Manis  | TEH001   | 0    | Umum         | 0.0       | 8000.0    |
```

---

## 🔄 Update Data di Manajemen Stok

Setelah produk ditambahkan dari kasir, lengkapi data di Menu Manajemen Stok:

1. Buka Menu **Manajemen Stok**
2. Cari produk yang baru ditambahkan
3. Tap Edit
4. Input:
   - **Stok**: 100
   - **Harga Beli**: 10000
   - **Kategori**: Minuman
5. Simpan

✅ Produk sekarang lengkap dengan stok dan harga beli

---

## ✅ Expected Result

### Form Kasir:
```
┌──────────────────────────────┐
│  Tambah Produk               │
├──────────────────────────────┤
│  Nama Produk *               │
│  [Kopi Susu____________]     │
│                              │
│  SKU/Barcode (Opsional)      │
│  [____________] [📷 QR]      │
│                              │
│  Harga Jual *                │
│  Rp [15000__________]        │
│                              │
│  Jumlah    [➖] 1 [➕]       │
├──────────────────────────────┤
│  [Batal]    [Simpan] ✅      │
└──────────────────────────────┘
```

---

## 🚫 Error Scenarios

### ❌ Tombol Simpan Disabled Jika:
1. Nama Produk kosong
2. Harga Jual kosong
3. Keduanya kosong

### ✅ Tombol Simpan Aktif Jika:
1. Nama Produk diisi
2. Harga Jual diisi
3. SKU boleh kosong ✅

---

## 📱 UI Test Points

- [ ] Placeholder "SKU/Kode Barcode (Opsional)" muncul
- [ ] Form tidak ada field Stok
- [ ] Form tidak ada field Kategori
- [ ] Form tidak ada field Harga Beli
- [ ] Tombol QR untuk scan barcode ada
- [ ] Autocomplete produk berfungsi
- [ ] Counter +/- jumlah berfungsi
- [ ] Validasi form sesuai (hanya nama & harga)

---

## 🎯 Success Criteria

✅ Kasir bisa tambah produk dengan cepat (hanya 2 field wajib)
✅ SKU bersifat opsional (boleh kosong)
✅ Produk baru otomatis tersimpan dengan default value
✅ Form lebih sederhana dan user-friendly
✅ Tidak ada field stok/harga beli di kasir
✅ Manajemen stok tetap bisa update data lengkap

---

**Tanggal:** 17 Februari 2026
**Status:** ✅ Ready to Test

