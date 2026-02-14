package com.example.ngasiryuk.screen.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R
import com.example.ngasiryuk.data.local.entity.KategoriEntity
import com.example.ngasiryuk.data.local.entity.ProdukEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahKeranjangBottomSheet(
    produkList: List<ProdukEntity>,
    kategoriList: List<KategoriEntity>,
    scannedBarcode: String = "",
    onDismiss: () -> Unit,
    onSave: (ProdukEntity, Int) -> Unit,
    onAddProduk: (String, String, Int, Int, String, Double, Double) -> Unit,
    onScanBarcode: () -> Unit
) {
    var showDropdown by remember { mutableStateOf(false) }
    var selectedProduk by remember { mutableStateOf<ProdukEntity?>(null) }

    // State untuk form
    var namaProduk by remember { mutableStateOf("") }
    var skuBarcode by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var selectedKategoriId by remember { mutableIntStateOf(0) }
    var hargaBeli by remember { mutableStateOf("") }
    var hargaJual by remember { mutableStateOf("") }
    var jumlah by remember { mutableIntStateOf(1) }
    var expandedKategori by remember { mutableStateOf(false) }

    // Isi SKU dari hasil scan barcode
    LaunchedEffect(scannedBarcode) {
        if (scannedBarcode.isNotEmpty()) {
            skuBarcode = scannedBarcode
        }
    }

    // Filtered produk untuk autocomplete
    val filteredProduk = remember(namaProduk, produkList) {
        if (namaProduk.isEmpty()) {
            emptyList()
        } else {
            produkList.filter {
                it.namaProduk.contains(namaProduk, ignoreCase = true)
            }.take(5) // Limit 5 hasil
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 16.dp)
    ) {
        // Title
        Text(
            text = "Tambah Produk",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = plusjakarta,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Form dengan LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 450.dp)
                .weight(1f, fill = false)
        ) {
            item {
                // Nama Produk dengan Autocomplete
                ExposedDropdownMenuBox(
                    expanded = showDropdown && filteredProduk.isNotEmpty(),
                    onExpandedChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    OutlinedTextField(
                        value = namaProduk,
                        onValueChange = {
                            namaProduk = it
                            showDropdown = it.isNotEmpty()
                            // Reset jika user mengubah nama
                            if (selectedProduk != null && selectedProduk!!.namaProduk != it) {
                                selectedProduk = null
                            }
                        },
                        placeholder = {
                            Text(
                                "Nama Produk",
                                color = Color.Gray,
                                fontFamily = plusjakarta,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = Color(0xFFFDB913),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5)
                        )
                    )

                    // Dropdown autocomplete
                    ExposedDropdownMenu(
                        expanded = showDropdown && filteredProduk.isNotEmpty(),
                        onDismissRequest = { showDropdown = false },
                        modifier = Modifier.heightIn(max = 200.dp)
                    ) {
                        filteredProduk.forEach { produk ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            text = produk.namaProduk,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black,
                                            fontFamily = plusjakarta
                                        )
                                        Text(
                                            text = "Stok: ${produk.stok} | ${produk.kategoriNama}",
                                            fontSize = 11.sp,
                                            color = Color.Gray,
                                            fontFamily = plusjakarta
                                        )
                                    }
                                },
                                onClick = {
                                    selectedProduk = produk
                                    namaProduk = produk.namaProduk
                                    skuBarcode = produk.sku
                                    stok = produk.stok.toString()
                                    kategori = produk.kategoriNama
                                    selectedKategoriId = produk.kategoriId
                                    hargaBeli = produk.hargaBeli.toInt().toString()
                                    hargaJual = produk.hargaJual.toInt().toString()
                                    showDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                // Info jika produk ditemukan
                if (selectedProduk != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Found",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Produk ditemukan - Harga bisa disesuaikan",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2E7D32),
                                fontFamily = plusjakarta
                            )
                        }
                    }
                }
            }

            item {
                // SKU/Kode Barcode dengan tombol scan
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = skuBarcode,
                        onValueChange = { skuBarcode = it },
                        placeholder = {
                            Text(
                                "SKU/Kode Barcode",
                                color = Color.Gray,
                                fontFamily = plusjakarta,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = Color(0xFFFDB913),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5)
                        )
                    )

                    // Tombol Scan QR
                    IconButton(
                        onClick = onScanBarcode,
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = Color(0xFFFDB913),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.qrcode),
                            contentDescription = "Scan QR",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            item {
                // Stok
                OutlinedTextField(
                    value = stok,
                    onValueChange = { stok = it },
                    placeholder = {
                        Text(
                            "Stok",
                            color = Color.Gray,
                            fontFamily = plusjakarta,
                            fontSize = 14.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Color(0xFFFDB913),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5)
                    )
                )
            }

            item {
                // Kategori Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedKategori,
                    onExpandedChange = { expandedKategori = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    OutlinedTextField(
                        value = kategori,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = {
                            Text(
                                "Kategori",
                                color = Color.Gray,
                                fontFamily = plusjakarta,
                                fontSize = 14.sp
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = Color(0xFFFDB913),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedKategori,
                        onDismissRequest = { expandedKategori = false }
                    ) {
                        if (kategoriList.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Belum ada kategori") },
                                onClick = { }
                            )
                        } else {
                            kategoriList.forEach { kat ->
                                DropdownMenuItem(
                                    text = { Text(kat.namaKategori) },
                                    onClick = {
                                        kategori = kat.namaKategori
                                        selectedKategoriId = kat.id
                                        expandedKategori = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Harga Beli dan Harga Jual
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Harga Beli
                    OutlinedTextField(
                        value = hargaBeli,
                        onValueChange = { hargaBeli = it },
                        placeholder = {
                            Text(
                                "Harga Beli",
                                color = Color.Gray,
                                fontFamily = plusjakarta,
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Text(
                                "Rp",
                                color = Color.Gray,
                                fontFamily = plusjakarta,
                                fontSize = 12.sp
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = Color(0xFFFDB913),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5)
                        )
                    )

                    // Harga Jual
                    OutlinedTextField(
                        value = hargaJual,
                        onValueChange = { hargaJual = it },
                        placeholder = {
                            Text(
                                "Harga Jual",
                                color = Color.Gray,
                                fontFamily = plusjakarta,
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Text(
                                "Rp",
                                color = Color.Gray,
                                fontFamily = plusjakarta,
                                fontSize = 12.sp
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = Color(0xFFFDB913),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5)
                        )
                    )
                }
            }

            item {
                // Jumlah Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Jumlah",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = plusjakarta
                    )

                    // Counter Controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Minus Button
                        IconButton(
                            onClick = {
                                if (jumlah > 1) jumlah--
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(0xFFFFEBEE),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.minus),
                                contentDescription = "Minus",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Jumlah Display
                        Text(
                            text = jumlah.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = plusjakarta,
                            modifier = Modifier.widthIn(min = 30.dp),
                            textAlign = TextAlign.Center
                        )

                        // Plus Button
                        IconButton(
                            onClick = {
                                val stokInt = stok.toIntOrNull() ?: 0
                                if (selectedProduk != null && stokInt > 0) {
                                    if (jumlah < stokInt) jumlah++
                                } else {
                                    jumlah++
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(0xFFFDB913),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Plus",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Batal Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5F5F5)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Batal",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = plusjakarta,
                    fontSize = 16.sp
                )
            }

            // Simpan Button
            Button(
                onClick = {
                    // Validasi form
                    val isFormValid = namaProduk.isNotEmpty() &&
                        skuBarcode.isNotEmpty() &&
                        stok.isNotEmpty() &&
                        kategori.isNotEmpty() &&
                        hargaBeli.isNotEmpty() &&
                        hargaJual.isNotEmpty()

                    if (isFormValid) {
                        if (selectedProduk != null) {
                            // Produk ada - update dengan data yang mungkin sudah diedit
                            val produkCustom = selectedProduk!!.copy(
                                hargaJual = hargaJual.toDoubleOrNull() ?: selectedProduk!!.hargaJual,
                                hargaBeli = hargaBeli.toDoubleOrNull() ?: selectedProduk!!.hargaBeli
                            )
                            onSave(produkCustom, jumlah)
                        } else {
                            // Produk baru - tambah ke database dulu
                            onAddProduk(
                                namaProduk,
                                skuBarcode,
                                stok.toIntOrNull() ?: 0,
                                selectedKategoriId,
                                kategori,
                                hargaBeli.toDoubleOrNull() ?: 0.0,
                                hargaJual.toDoubleOrNull() ?: 0.0
                            )

                            // Buat produk entity untuk keranjang
                            val produkBaru = ProdukEntity(
                                id = 0,
                                namaProduk = namaProduk,
                                sku = skuBarcode,
                                stok = stok.toIntOrNull() ?: 0,
                                kategoriId = selectedKategoriId,
                                kategoriNama = kategori,
                                hargaBeli = hargaBeli.toDoubleOrNull() ?: 0.0,
                                hargaJual = hargaJual.toDoubleOrNull() ?: 0.0
                            )
                            onSave(produkBaru, jumlah)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFDB913)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = namaProduk.isNotEmpty() &&
                    skuBarcode.isNotEmpty() &&
                    stok.isNotEmpty() &&
                    kategori.isNotEmpty() &&
                    hargaBeli.isNotEmpty() &&
                    hargaJual.isNotEmpty()
            ) {
                Text(
                    text = "Simpan",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusjakarta,
                    fontSize = 16.sp
                )
            }
        }
    }
}



