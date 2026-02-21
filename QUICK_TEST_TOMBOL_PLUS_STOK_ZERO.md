# 🧪 Quick Test: Tombol Plus untuk Stok = 0

## ⚡ Test Scenario

### ✅ Test 1: Produk Baru (Stok = 0)
```
1. Buka Kasir → Tambah Produk
2. Input:
   - Nama: "Cappuccino"
   - Harga Jual: 20000
3. Klik tombol + sebanyak 10x
4. Lihat jumlah

Expected Result:
✅ Jumlah naik dari 1 → 10
✅ Tombol + tetap aktif
✅ Tidak ada batasan
```

### ✅ Test 2: Produk Existing (Stok > 0)
```
1. Buka Kasir → Tambah Produk
2. Pilih produk yang punya stok (contoh: stok = 5)
3. Klik tombol + sebanyak 10x
4. Lihat jumlah

Expected Result:
✅ Jumlah naik dari 1 → 5
❌ Setelah 5, tombol + tidak bisa tambah
✅ Card info hijau: "Produk ditemukan - Stok: 5"
```

### ✅ Test 3: Produk Existing (Stok = 0)
```
1. Buka Kasir → Tambah Produk
2. Pilih produk yang stok = 0
3. Klik tombol + sebanyak 10x
4. Lihat jumlah

Expected Result:
✅ Jumlah naik dari 1 → 10
✅ Tombol + tetap aktif
✅ Card info orange: "Stok belum diinput"
✅ Info: "Tetap bisa dijual, update stok di Manajemen Stok"
```

---

## 🎨 Visual Check

### Produk dengan Stok > 0:
```
┌────────────────────────────────┐
│ ✅ Produk ditemukan - Stok: 20 │
└────────────────────────────────┘
Background: HIJAU (#E8F5E9)
Icon: Hijau
```

### Produk dengan Stok = 0:
```
┌──────────────────────────────────────┐
│ ⚠️ Produk ditemukan - Stok belum    │
│    diinput                           │
│    Tetap bisa dijual, update stok di │
│    Manajemen Stok                    │
└──────────────────────────────────────┘
Background: ORANGE (#FFF3E0)
Icon: Orange
```

---

## 📋 Checklist

- [ ] Produk baru: tombol + tidak dibatasi ✅
- [ ] Produk stok > 0: tombol + dibatasi sesuai stok ✅
- [ ] Produk stok = 0: tombol + tidak dibatasi ✅
- [ ] Card hijau untuk stok > 0 ✅
- [ ] Card orange untuk stok = 0 ✅
- [ ] Info text muncul untuk stok = 0 ✅
- [ ] Tombol minus (-) tetap berfungsi ✅

---

## 🚀 One-Minute Test

```
1. Buka Kasir
2. Tambah produk baru "Test Item" - Rp 10,000
3. Klik + sampai jumlah = 50
   ✅ Harus bisa (tidak dibatasi)

4. Simpan ke keranjang
5. Tambah produk yang sama lagi
6. Lihat card info: ORANGE (stok = 0)
7. Klik + sampai jumlah = 100
   ✅ Harus bisa (tidak dibatasi)

8. Buka Manajemen Stok
9. Edit "Test Item" → Input Stok = 10
10. Kembali ke Kasir
11. Tambah "Test Item" lagi
12. Lihat card info: HIJAU (stok = 10)
13. Klik + sampai jumlah = 15
    ✅ Maksimal 10 (dibatasi)
```

---

## ✅ Success Criteria
✅ Produk stok = 0 bisa tambah jumlah unlimited
✅ Produk stok > 0 dibatasi sesuai stok
✅ Visual feedback (hijau/orange) sesuai kondisi
✅ Tidak ada crash atau error

---

**Status:** ✅ Ready to Test
**Tanggal:** 17 Februari 2026

