package com.example.ngasiryuk.screen.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.window.Dialog
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R
import com.example.ngasiryuk.data.local.entity.KategoriEntity
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import com.example.ngasiryuk.screen.menu.kasir.QRScannerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahProdukKeKeranjangDialog(
    produkList: List<ProdukEntity>,
    kategoriList: List<KategoriEntity>,
    onDismiss: () -> Unit,
    onSave: (ProdukEntity, Int) -> Unit,
    onAddProduk: (String, String, Int, Int, String, Double, Double) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedProduk by remember { mutableStateOf<ProdukEntity?>(null) }
    var jumlah by remember { mutableIntStateOf(1) }
    var showAddProdukForm by remember { mutableStateOf(false) }
    var showQRScanner by remember { mutableStateOf(false) }

    // State untuk form tambah produk
    var namaProduk by remember { mutableStateOf("") }
    var skuBarcode by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var selectedKategoriId by remember { mutableIntStateOf(0) }
    var hargaBeli by remember { mutableStateOf("") }
    var hargaJual by remember { mutableStateOf("") }
    var expandedKategori by remember { mutableStateOf(false) }

    val filteredProduk = remember(searchQuery, produkList) {
        if (searchQuery.isEmpty()) {
            produkList
        } else {
            produkList.filter {
                it.namaProduk.contains(searchQuery, ignoreCase = true) ||
                it.sku.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        if (!showQRScanner) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Title
                    Text(
                    text = if (showAddProdukForm) "Tambah Produk Baru" else "Tambah Ke Keranjang",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (showAddProdukForm) {
                    // Form Tambah Produk
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        item {
                            // Nama Produk
                            OutlinedTextField(
                                value = namaProduk,
                                onValueChange = { namaProduk = it },
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
                                    onClick = { showQRScanner = true },
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
                                    .padding(bottom = 24.dp),
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
                    }
                } else {
                    // Search Bar dan Product List (original content)

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "Cari Produk",
                            color = Color.Gray,
                            fontFamily = plusjakarta,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray
                        )
                    },
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

                // Product List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .padding(bottom = 16.dp)
                ) {
                    items(filteredProduk) { produk ->
                        ProductItem(
                            produk = produk,
                            isSelected = selectedProduk?.id == produk.id,
                            onClick = { selectedProduk = produk }
                        )
                    }

                    if (filteredProduk.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Produk tidak ditemukan",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontFamily = plusjakarta,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                                // Tombol Tambah Produk
                                OutlinedButton(
                                    onClick = {
                                        showAddProdukForm = true
                                        namaProduk = searchQuery
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color(0xFFFFF9E6)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add Product",
                                        tint = Color(0xFFFDB913)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Tambah Produk Baru",
                                        color = Color.Black,
                                        fontFamily = plusjakarta,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                // Selected Product Info
                if (selectedProduk != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = selectedProduk!!.namaProduk,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontFamily = plusjakarta
                            )
                            Text(
                                text = "Stok: ${selectedProduk!!.stok} | Rp ${selectedProduk!!.hargaJual.toInt()}",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontFamily = plusjakarta
                            )
                        }
                    }
                }

                // Jumlah Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
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
                                if (selectedProduk != null && jumlah < selectedProduk!!.stok) {
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

                // Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Batal Button
                    Button(
                        onClick = {
                            if (showAddProdukForm) {
                                showAddProdukForm = false
                                // Reset form
                                namaProduk = ""
                                skuBarcode = ""
                                stok = ""
                                kategori = ""
                                selectedKategoriId = 0
                                hargaBeli = ""
                                hargaJual = ""
                            } else {
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF5F5F5)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (showAddProdukForm) "Kembali" else "Batal",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = plusjakarta,
                            fontSize = 16.sp
                        )
                    }

                    // Simpan Button
                    Button(
                        onClick = {
                            if (showAddProdukForm) {
                                // Validasi dan simpan produk baru
                                if (namaProduk.isNotEmpty() && skuBarcode.isNotEmpty() &&
                                    stok.isNotEmpty() && kategori.isNotEmpty() &&
                                    hargaBeli.isNotEmpty() && hargaJual.isNotEmpty()) {

                                    onAddProduk(
                                        namaProduk,
                                        skuBarcode,
                                        stok.toIntOrNull() ?: 0,
                                        selectedKategoriId,
                                        kategori,
                                        hargaBeli.toDoubleOrNull() ?: 0.0,
                                        hargaJual.toDoubleOrNull() ?: 0.0
                                    )

                                    // Reset form dan kembali
                                    showAddProdukForm = false
                                    namaProduk = ""
                                    skuBarcode = ""
                                    stok = ""
                                    kategori = ""
                                    selectedKategoriId = 0
                                    hargaBeli = ""
                                    hargaJual = ""
                                }
                            } else {
                                // Tambah ke keranjang
                                if (selectedProduk != null) {
                                    onSave(selectedProduk!!, jumlah)
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
                        enabled = if (showAddProdukForm) {
                            namaProduk.isNotEmpty() && skuBarcode.isNotEmpty() &&
                            stok.isNotEmpty() && kategori.isNotEmpty() &&
                            hargaBeli.isNotEmpty() && hargaJual.isNotEmpty()
                        } else {
                            selectedProduk != null
                        }
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
        }

        // QR Scanner untuk SKU/Barcode - diluar Card
        if (showQRScanner) {
            QRScannerScreen(
                onBarcodeScanned = { barcode ->
                    // Mode tambah produk - isi SKU
                    skuBarcode = barcode
                    showQRScanner = false
                },
                onDismiss = { showQRScanner = false }
            )
        }
    }
}

@Composable
private fun ProductItem(
    produk: ProdukEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                color = if (isSelected) Color(0xFFFFF9E6) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = produk.namaProduk,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = plusjakarta
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SKU: ${produk.sku}",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = plusjakarta
            )
            Text(
                text = "Stok: ${produk.stok}",
                fontSize = 12.sp,
                color = if (produk.stok > 0) Color.Gray else Color.Red,
                fontFamily = plusjakarta
            )
        }
        Text(
            text = "Rp ${produk.hargaJual.toInt()}",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFFDB913),
            fontFamily = plusjakarta
        )
    }
    HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
}




