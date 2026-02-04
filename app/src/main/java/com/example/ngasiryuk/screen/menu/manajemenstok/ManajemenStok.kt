package com.example.ngasiryuk.screen.menu.manajemenstok

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ManajemenStok() {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

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
                            text = "Manajemen Stok",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = plusjakarta
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Custom Tab Row dengan Indicator
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Tab Stok
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            }
                        ) {
                            Text(
                                text = "Stok",
                                fontSize = 16.sp,
                                fontWeight = if (pagerState.currentPage == 0) FontWeight.Bold else FontWeight.Normal,
                                color = Color.Black,
                                fontFamily = plusjakarta
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Indicator
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(3.dp)
                                    .background(
                                        color = if (pagerState.currentPage == 0) Color.White else Color.Transparent,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        }

                        // Tab Barang Masuk
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                            }
                        ) {
                            Text(
                                text = "Barang Masuk",
                                fontSize = 16.sp,
                                fontWeight = if (pagerState.currentPage == 1) FontWeight.Bold else FontWeight.Normal,
                                color = Color.Black,
                                fontFamily = plusjakarta
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Indicator
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(3.dp)
                                    .background(
                                        color = if (pagerState.currentPage == 1) Color.White else Color.Transparent,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        }

                        // Tab Barang Keluar
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(2)
                                }
                            }
                        ) {
                            Text(
                                text = "Barang Keluar",
                                fontSize = 16.sp,
                                fontWeight = if (pagerState.currentPage == 2) FontWeight.Bold else FontWeight.Normal,
                                color = Color.Black,
                                fontFamily = plusjakarta
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Indicator
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(3.dp)
                                    .background(
                                        color = if (pagerState.currentPage == 2) Color.White else Color.Transparent,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // HorizontalPager untuk swipe antar halaman
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> StokPage(onAddClick = { showDialog = true })
                    1 -> BarangMasukPage()
                    2 -> BarangKeluarPage()
                }
            }
        }
    }

    // Dialog Tambah Stok
    if (showDialog) {
        TambahStokDialog(
            onDismiss = { showDialog = false },
            onSave = { jumlah ->
                // Handle save
                showDialog = false
            }
        )
    }
}

// Halaman 1: Stok
@Composable
fun StokPage(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tombol Export
        Button(
            onClick = { /* Handle export */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFDB913)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.export),
                contentDescription = "Export",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Export",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = plusjakarta,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List Stok Barang
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) { index ->
                StokBarangCard(
                    namaBarang = "Galon 19Lt",
                    sku = "889029900",
                    kategori = "Galon",
                    jumlah = 4,
                    onAddClick = onAddClick
                )
            }
        }
    }
}

@Composable
fun StokBarangCard(
    namaBarang: String,
    sku: String,
    kategori: String,
    jumlah: Int,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
            // Info Barang
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
                    text = "SKU : $sku",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Kategori : $kategori",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
            }

            // Counter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Minus Button
                IconButton(
                    onClick = { /* Handle minus */ },
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color(0xFFFFEBEE),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.minus),
                        contentDescription = "Minus",
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Jumlah
                Text(
                    text = jumlah.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta,
                    modifier = Modifier.widthIn(min = 24.dp),
                    textAlign = TextAlign.Center
                )

                // Plus Button
                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color(0xFFE8F5E9),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Plus",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// Halaman 2: Barang Masuk
@Composable
fun BarangMasukPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tombol Export
        Button(
            onClick = { /* Handle export */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFDB913)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.export),
                contentDescription = "Export",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Export",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = plusjakarta,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text(
                    "Cari Riwayat",
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
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFFFDB913),
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // List Barang Masuk
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) { index ->
                BarangMasukCard(
                    namaBarang = "Galon 19Lt",
                    sku = "889029900",
                    tanggal = "12-12-2026",
                    jumlah = 4
                )
            }
        }
    }
}

@Composable
fun BarangMasukCard(
    namaBarang: String,
    sku: String,
    tanggal: String,
    jumlah: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
            // Icon Masuk (Arrow Down)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFFE8F5E9),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.masuk),
                    contentDescription = "Masuk",
                    modifier = Modifier.size(20.dp), tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info Barang
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
                    text = "SKU : $sku",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tanggal,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
            }

            // Jumlah dengan badge
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "+$jumlah",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50),
                    fontFamily = plusjakarta
                )
            }
        }
    }
}

// Halaman 3: Barang Keluar
@Composable
fun BarangKeluarPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tombol Export
        Button(
            onClick = { /* Handle export */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFDB913)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.export),
                contentDescription = "Export",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Export",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = plusjakarta,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text(
                    "Cari Riwayat",
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
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFFFDB913),
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // List Barang Keluar
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) { index ->
                BarangKeluarCard(
                    namaBarang = "Galon 19Lt",
                    sku = "889029900",
                    tanggal = "12-12-2026",
                    jumlah = 4
                )
            }
        }
    }
}

@Composable
fun BarangKeluarCard(
    namaBarang: String,
    sku: String,
    tanggal: String,
    jumlah: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
            // Icon Keluar (Arrow Up)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFFFFEBEE),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.keluar),
                    contentDescription = "Keluar",
                    tint = Color(0xFFFF5252),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info Barang
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
                    text = "SKU : $sku",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tanggal,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
            }

            // Jumlah dengan badge
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "-$jumlah",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF5252),
                    fontFamily = plusjakarta
                )
            }
        }
    }
}

// Dialog Tambah Stok
@Composable
fun TambahStokDialog(
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var jumlah by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Title
                Text(
                    text = "Tambahkan Stok",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Subtitle
                Text(
                    text = "Tentukan Jumlah Stok Yang Ingin Ditambahkan",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Label Jumlah
                Text(
                    text = "Jumlah",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = plusjakarta,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Input Jumlah
                OutlinedTextField(
                    value = jumlah,
                    onValueChange = { jumlah = it },
                    placeholder = {
                        Text(
                            "",
                            color = Color.Gray,
                            fontFamily = plusjakarta
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Color(0xFFFDB913),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

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
                            jumlah.toIntOrNull()?.let { onSave(it) }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFDB913)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = jumlah.isNotEmpty()
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
}

@Preview(showBackground = true)
@Composable
private fun ManajemenStokPreview() {
    ManajemenStok()
}