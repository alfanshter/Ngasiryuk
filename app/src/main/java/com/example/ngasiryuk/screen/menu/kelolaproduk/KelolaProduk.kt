package com.example.ngasiryuk.screen.menu.kelolaproduk

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelolaProduk() {
    var selectedTab by remember { mutableStateOf(0) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            // Custom Top Bar dengan Rounded Bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(199.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(Color(0xFFFDB913))
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(top = 35.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Tombol Back Bulat
                        IconButton(
                            onClick = { /* Handle back navigation */ },
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = Color(0xFFD4A419),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Title
                        Text(
                            text = "Kelola Produk",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = plusjakarta
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = { /* Handle search */ },
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = Color(0xFFD4A419),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tab Row (Stok & Riwayat)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Tab Stok
                        Button(
                            onClick = { selectedTab = 0 },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTab == 0) Color.White else Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            elevation = if (selectedTab == 0) ButtonDefaults.buttonElevation(4.dp) else null
                        ) {
                            Text(
                                text = "Stok",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = plusjakarta
                            )
                        }

                        // Tab Riwayat
                        Button(
                            onClick = { selectedTab = 1 },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTab == 1) Color.White else Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            elevation = if (selectedTab == 1) ButtonDefaults.buttonElevation(4.dp) else null
                        ) {
                            Text(
                                text = "Riwayat",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = plusjakarta
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = Color(0xFFFDB913),
                contentColor = Color.Black,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        // Content berdasarkan tab yang dipilih
        when (selectedTab) {
            0 -> StokContent(paddingValues) // Halaman Stok
            1 -> RiwayatContent(paddingValues) // Halaman Riwayat
        }
    }

    // Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        ) {
            TambahProdukContent(
                onDismiss = { showBottomSheet = false }
            )
        }
    }
}

// Halaman Stok Content
@Composable
fun StokContent(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(3) { index ->
            ProductCard(
                isHighlighted = index == 0
            )
        }
    }
}

// Halaman Riwayat Content
@Composable
fun RiwayatContent(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) { index ->
            RiwayatCard(
                namaBarang = "Autan liquid",
                tanggal = "04 Feb 2026",
                jumlah = if (index % 2 == 0) "+10" else "-5",
                keterangan = if (index % 2 == 0) "Stok Masuk" else "Terjual",
                isMasuk = index % 2 == 0
            )
        }
    }
}

// Card untuk Riwayat
@Composable
fun RiwayatCard(
    namaBarang: String,
    tanggal: String,
    jumlah: String,
    keterangan: String,
    isMasuk: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = namaBarang,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tanggal,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = keterangan,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
            }

            // Jumlah dengan warna berbeda
            Text(
                text = jumlah,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isMasuk) Color(0xFF4CAF50) else Color(0xFFFF5252),
                fontFamily = plusjakarta
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahProdukContent(onDismiss: () -> Unit) {
    var namaProduk by remember { mutableStateOf("") }
    var skuBarcode by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var hargaBeli by remember { mutableStateOf("") }
    var hargaJual by remember { mutableStateOf("") }
    var expandedKategori by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
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

        // Nama Produk
        OutlinedTextField(
            value = namaProduk,
            onValueChange = { namaProduk = it },
            placeholder = {
                Text(
                    "Nama Produk",
                    color = Color.Gray,
                    fontFamily = plusjakarta
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

        // SKU/Kode Barcode dengan Icon Scanner
        OutlinedTextField(
            value = skuBarcode,
            onValueChange = { skuBarcode = it },
            placeholder = {
                Text(
                    "Sku/Kode Barcode",
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
            },
            trailingIcon = {
                IconButton(onClick = { /* Handle scan */ }) {
                    Icon(
                        painter = painterResource(R.drawable.qrcode),
                        contentDescription = "Scan Barcode",
                        tint = Color.Unspecified
                    )
                }
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

        // Stok
        OutlinedTextField(
            value = stok,
            onValueChange = { stok = it },
            placeholder = {
                Text(
                    "Stok",
                    color = Color.Gray,
                    fontFamily = plusjakarta
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

        // Kategori Dropdown dengan Icon Add
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedKategori,
                onExpandedChange = { expandedKategori = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = kategori,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            "Kategori",
                            color = Color.Gray,
                            fontFamily = plusjakarta
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
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
                    DropdownMenuItem(
                        text = { Text("Body Lotion") },
                        onClick = {
                            kategori = "Body Lotion"
                            expandedKategori = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sabun") },
                        onClick = {
                            kategori = "Sabun"
                            expandedKategori = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Shampoo") },
                        onClick = {
                            kategori = "Shampoo"
                            expandedKategori = false
                        }
                    )
                }
            }

            // Tombol Add Kategori
            IconButton(
                onClick = { /* Handle add category */ },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = Color(0xFFFDB913),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Category",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

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
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Text(
                        "Rp",
                        color = Color.Gray,
                        fontFamily = plusjakarta,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
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
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Text(
                        "Rp",
                        color = Color.Gray,
                        fontFamily = plusjakarta,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
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
                    // Handle save
                    onDismiss()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFDB913)
                ),
                shape = RoundedCornerShape(12.dp)
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

@Composable
fun ProductCard(isHighlighted: Boolean = false) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Autan liquid",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = plusjakarta
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Ready Badge
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF4CAF50),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Ready",
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "SKU : 8899222019I0101",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "STOK : 8",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Kategori : Body Lotion",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )
                }

                // Action Buttons
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { /* Handle edit */ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = { /* Handle delete */ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = { /* Handle QR */ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.qrcode),
                            contentDescription = "QR Code",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Harga Beli : Rp 1.000",
                    fontSize = 14.sp,
                    color = Color(0xFFFDB913),
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusjakarta
                )
                Text(
                    text = "Harga Jual : Rp 1.500",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusjakarta
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KelolaProdukPreview() {
    KelolaProduk()

}