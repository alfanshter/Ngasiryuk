package com.example.ngasiryuk.screen.menu.manajemenstok

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import com.example.ngasiryuk.di.AppContainer
import com.example.ngasiryuk.screen.component.PasswordProtectedScreen
import com.example.ngasiryuk.utils.ExportUtils
import com.example.ngasiryuk.utils.MenuConstants
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ManajemenStok(navController: NavController) {
    val viewModel: ManajemenStokViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AppContainer.provideManajemenStokViewModel() as T
        }
    })

    val passwordViewModel = remember {
        AppContainer.provideMenuPasswordViewModel()
    }

    PasswordProtectedScreen(
        menuName = MenuConstants.MENU_MANAJEMEN_STOK,
        viewModel = passwordViewModel,
        onAccessGranted = {
            ManajemenStokContent(navController, viewModel)
        },
        onAccessDenied = {
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ManajemenStokContent(navController: NavController, viewModel: ManajemenStokViewModel) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var selectedProduk by remember { mutableStateOf<ProdukEntity?>(null) }
    var isAdding by remember { mutableStateOf(true) }

    val produkList by viewModel.filteredProdukList.collectAsState()
    val barangMasukList by viewModel.barangMasukList.collectAsState()
    val barangKeluarList by viewModel.barangKeluarList.collectAsState()

    var isExporting by remember { mutableStateOf(false) }

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
                            onClick = { navController.popBackStack() },
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
                    0 -> StokPage(
                        produkList = produkList,
                        searchQuery = viewModel.searchQueryStok.collectAsState().value,
                        onSearchQueryChange = { viewModel.updateSearchQueryStok(it) },
                        onAddClick = { produk ->
                            selectedProduk = produk
                            isAdding = true
                            showDialog = true
                        },
                        onMinusClick = { produk ->
                            selectedProduk = produk
                            isAdding = false
                            showDialog = true
                        },
                        onExport = {
                            scope.launch {
                                isExporting = true
                                try {
                                    val file = ExportUtils.exportStokToExcel(context, produkList)
                                    ExportUtils.shareFile(context, file)
                                    Toast.makeText(context, "Export berhasil", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Export gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isExporting = false
                                }
                            }
                        }
                    )
                    1 -> BarangMasukPage(
                        riwayatList = barangMasukList,
                        searchQuery = viewModel.searchQueryMasuk.collectAsState().value,
                        onSearchQueryChange = { viewModel.updateSearchQueryMasuk(it) },
                        onExport = {
                            scope.launch {
                                isExporting = true
                                try {
                                    val file = ExportUtils.exportBarangMasukToExcel(context, barangMasukList)
                                    ExportUtils.shareFile(context, file)
                                    Toast.makeText(context, "Export berhasil", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Export gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isExporting = false
                                }
                            }
                        }
                    )
                    2 -> BarangKeluarPage(
                        riwayatList = barangKeluarList,
                        searchQuery = viewModel.searchQueryKeluar.collectAsState().value,
                        onSearchQueryChange = { viewModel.updateSearchQueryKeluar(it) },
                        onExport = {
                            scope.launch {
                                isExporting = true
                                try {
                                    val file = ExportUtils.exportBarangKeluarToExcel(context, barangKeluarList)
                                    ExportUtils.shareFile(context, file)
                                    Toast.makeText(context, "Export berhasil", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Export gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isExporting = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    // Dialog Tambah Stok
    if (showDialog) {
        TambahStokDialog(
            produk = selectedProduk,
            isAdding = isAdding,
            onDismiss = {
                showDialog = false
                selectedProduk = null
            },
            onSave = { jumlah, keterangan ->
                selectedProduk?.let { produk ->
                    if (isAdding) {
                        viewModel.tambahStok(produk.id, jumlah, keterangan)
                        Toast.makeText(context, "Stok berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    } else {
                        if (jumlah <= produk.stok) {
                            viewModel.kurangiStok(produk.id, jumlah, keterangan)
                            Toast.makeText(context, "Stok berhasil dikurangi", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Jumlah melebihi stok tersedia", Toast.LENGTH_SHORT).show()
                            return@let
                        }
                    }
                }
                showDialog = false
                selectedProduk = null
            }
        )
    }

    // Loading indicator
    if (isExporting) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFFDB913))
            }
        }
    }
}

// Halaman 1: Stok
@Composable
fun StokPage(
    produkList: List<ProdukEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddClick: (ProdukEntity) -> Unit,
    onMinusClick: (ProdukEntity) -> Unit,
    onExport: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tombol Export
        Button(
            onClick = onExport,
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
            value = searchQuery,
            onValueChange = onSearchQueryChange,
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

        // List Stok Barang
        if (produkList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada produk",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(produkList) { produk ->
                    StokBarangCard(
                        produk = produk,
                        onAddClick = { onAddClick(produk) },
                        onMinusClick = { onMinusClick(produk) }
                    )
                }
            }
        }
    }
}

@Composable
fun StokBarangCard(
    produk: ProdukEntity,
    onAddClick: () -> Unit,
    onMinusClick: () -> Unit
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
                    text = produk.namaProduk,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "SKU : ${produk.sku}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Kategori : ${produk.kategoriNama}",
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
                    onClick = onMinusClick,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color(0xFFFFEBEE),
                            shape = CircleShape
                        ),
                    enabled = produk.stok > 0
                ) {
                    Icon(
                        painter = painterResource(R.drawable.minus),
                        contentDescription = "Minus",
                        tint = if (produk.stok > 0) Color(0xFFFF5252) else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Jumlah
                Text(
                    text = produk.stok.toString(),
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
fun BarangMasukPage(
    riwayatList: List<RiwayatStokEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onExport: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tombol Export
        Button(
            onClick = onExport,
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
            value = searchQuery,
            onValueChange = onSearchQueryChange,
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
        if (riwayatList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada riwayat barang masuk",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(riwayatList) { riwayat ->
                    BarangMasukCard(riwayat = riwayat)
                }
            }
        }
    }
}

@Composable
fun BarangMasukCard(riwayat: RiwayatStokEntity) {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    val tanggal = dateFormat.format(Date(riwayat.createdAt))

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
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info Barang
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = riwayat.namaProduk,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = riwayat.keterangan,
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
                    text = "+${riwayat.jumlah}",
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
fun BarangKeluarPage(
    riwayatList: List<RiwayatStokEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onExport: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tombol Export
        Button(
            onClick = onExport,
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
            value = searchQuery,
            onValueChange = onSearchQueryChange,
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
        if (riwayatList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada riwayat barang keluar",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(riwayatList) { riwayat ->
                    BarangKeluarCard(riwayat = riwayat)
                }
            }
        }
    }
}

@Composable
fun BarangKeluarCard(riwayat: RiwayatStokEntity) {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    val tanggal = dateFormat.format(Date(riwayat.createdAt))

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
                    text = riwayat.namaProduk,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = riwayat.keterangan,
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
                    text = kotlin.math.abs(riwayat.jumlah).toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF5252),
                    fontFamily = plusjakarta
                )
            }
        }
    }
}

// Dialog Tambah/Kurangi Stok
@Composable
fun TambahStokDialog(
    produk: ProdukEntity?,
    isAdding: Boolean,
    onDismiss: () -> Unit,
    onSave: (Int, String) -> Unit
) {
    var jumlah by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf(if (isAdding) "Stok Masuk" else "Stok Keluar") }

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
                    text = if (isAdding) "Tambahkan Stok" else "Kurangi Stok",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Subtitle
                Text(
                    text = produk?.namaProduk ?: "",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Stok saat ini: ${produk?.stok ?: 0}",
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
                    onValueChange = { if (it.all { char -> char.isDigit() }) jumlah = it },
                    placeholder = {
                        Text(
                            "Masukkan jumlah",
                            color = Color.Gray,
                            fontFamily = plusjakarta
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Color(0xFFFDB913),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

                // Label Keterangan
                Text(
                    text = "Keterangan",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = plusjakarta,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Input Keterangan
                OutlinedTextField(
                    value = keterangan,
                    onValueChange = { keterangan = it },
                    placeholder = {
                        Text(
                            "Masukkan keterangan",
                            color = Color.Gray,
                            fontFamily = plusjakarta
                        )
                    },
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
                            jumlah.toIntOrNull()?.let {
                                if (it > 0) {
                                    onSave(it, keterangan.ifBlank {
                                        if (isAdding) "Stok Masuk" else "Stok Keluar"
                                    })
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
                        enabled = jumlah.isNotEmpty() && jumlah.toIntOrNull() != null && jumlah.toInt() > 0
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
    ManajemenStok(navController = rememberNavController())
}