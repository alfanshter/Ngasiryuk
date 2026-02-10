package com.example.ngasiryuk.screen.menu.kelolaproduk

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R
import com.example.ngasiryuk.data.local.entity.KategoriEntity
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import com.example.ngasiryuk.di.AppContainer
import com.example.ngasiryuk.screen.component.PasswordProtectedScreen
import com.example.ngasiryuk.utils.MenuConstants
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelolaProduk(
    navController: NavController,
    viewModel: KelolaProdukViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AppContainer.provideKelolaProdukViewModel() as T
        }
    })
) {
    val passwordViewModel = remember {
        AppContainer.provideMenuPasswordViewModel()
    }

    PasswordProtectedScreen(
        menuName = MenuConstants.MENU_BARANG_DAN_JASA,
        viewModel = passwordViewModel,
        onAccessGranted = {
            KelolaProdukContent(navController, viewModel)
        },
        onAccessDenied = {
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KelolaProdukContent(
    navController: NavController,
    viewModel: KelolaProdukViewModel
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val produkList by viewModel.produkList.collectAsState()
    val riwayatList by viewModel.riwayatList.collectAsState()
    val kategoriList by viewModel.kategoriList.collectAsState()
    val selectedProduk by viewModel.selectedProduk.collectAsState()

    // Filter produk berdasarkan search query
    val filteredProdukList = remember(produkList, searchQuery) {
        if (searchQuery.isEmpty()) {
            produkList
        } else {
            produkList.filter { produk ->
                produk.namaProduk.contains(searchQuery, ignoreCase = true) ||
                produk.sku.contains(searchQuery, ignoreCase = true) ||
                produk.kategoriNama.contains(searchQuery, ignoreCase = true)
            }
        }
    }

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
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                            onClick = { showSearchDialog = true },
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
                onClick = {
                    showBottomSheet = true
                    viewModel.selectProduk(null) // Reset selection saat tambah baru
                },
                containerColor = Color(0xFFFDB913),
                contentColor = Color.Black,
                modifier = Modifier
                    .padding(bottom = 35.dp) // Angkat FAB 16dp dari bawah
                    .size(56.dp)
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
            0 -> StokContent(paddingValues, filteredProdukList, viewModel, context,
                onEditSuccess = { showBottomSheet = true }) // Halaman Stok
            1 -> RiwayatContent(paddingValues, riwayatList) // Halaman Riwayat
        }
    }

    // Search Dialog
    if (showSearchDialog) {
        AlertDialog(
            onDismissRequest = {
                showSearchDialog = false
            },
            title = {
                Text(
                    text = "Cari Produk",
                    fontFamily = plusjakarta,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "Nama produk, SKU, atau kategori",
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
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Color(0xFFFDB913),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5)
                    ),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = { showSearchDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFDB913)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Cari",
                        color = Color.Black,
                        fontFamily = plusjakarta,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        searchQuery = ""
                        showSearchDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Reset",
                        color = Color.Black,
                        fontFamily = plusjakarta,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                viewModel.selectProduk(null)
            },
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
                onDismiss = {
                    showBottomSheet = false
                    viewModel.selectProduk(null)
                },
                viewModel = viewModel,
                kategoriList = kategoriList,
                selectedProduk = selectedProduk,
                context = context
            )
        }
    }
}

// Halaman Stok Content
@Composable
fun StokContent(
    paddingValues: PaddingValues,
    produkList: List<ProdukEntity>,
    viewModel: KelolaProdukViewModel,
    context: android.content.Context,
    onEditSuccess: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (produkList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada produk",
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )
                }
            }
        } else {
            items(produkList) { produk ->
                ProductCard(
                    produk = produk,
                    onEdit = {
                        viewModel.selectProduk(produk)
                        onEditSuccess()
                    },
                    onDelete = {
                        viewModel.deleteProduk(produk)
                        Toast.makeText(context, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

// Halaman Riwayat Content
@Composable
fun RiwayatContent(
    paddingValues: PaddingValues,
    riwayatList: List<RiwayatStokEntity>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (riwayatList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada riwayat stok",
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )
                }
            }
        } else {
            items(riwayatList) { riwayat ->
                val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
                val tanggal = dateFormat.format(Date(riwayat.createdAt))

                RiwayatCard(
                    namaBarang = riwayat.namaProduk,
                    tanggal = tanggal,
                    jumlah = if (riwayat.jumlah > 0) "+${riwayat.jumlah}" else "${riwayat.jumlah}",
                    keterangan = riwayat.keterangan,
                    isMasuk = riwayat.jumlah > 0
                )
            }
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
fun TambahProdukContent(
    onDismiss: () -> Unit,
    viewModel: KelolaProdukViewModel,
    kategoriList: List<KategoriEntity>,
    selectedProduk: ProdukEntity?,
    context: android.content.Context
) {
    var namaProduk by remember { mutableStateOf(selectedProduk?.namaProduk ?: "") }
    var skuBarcode by remember { mutableStateOf(selectedProduk?.sku ?: "") }
    var stok by remember { mutableStateOf(selectedProduk?.stok?.toString() ?: "") }
    var kategori by remember { mutableStateOf(selectedProduk?.kategoriNama ?: "") }
    var selectedKategoriId by remember { mutableStateOf(selectedProduk?.kategoriId ?: 0) }
    var hargaBeli by remember { mutableStateOf(selectedProduk?.hargaBeli?.toInt()?.toString() ?: "") }
    var hargaJual by remember { mutableStateOf(selectedProduk?.hargaJual?.toInt()?.toString() ?: "") }
    var expandedKategori by remember { mutableStateOf(false) }
    var showScanner by remember { mutableStateOf(false) }

    if (showScanner) {
        // Show full screen scanner
        Box(modifier = Modifier.fillMaxSize()) {
            QRScannerScreen(
                onBarcodeScanned = { barcode ->
                    skuBarcode = barcode
                    showScanner = false
                },
                onDismiss = {
                    showScanner = false
                }
            )
        }
    } else {
        // Show form

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
    ) {
        // Title
        Text(
            text = if (selectedProduk != null) "Edit Produk" else "Tambah Produk",
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
                IconButton(onClick = { showScanner = true }) {
                    Icon(
                        painter = painterResource(R.drawable.qrcode),
                        contentDescription = "Scan Barcode",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
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
                    if (namaProduk.isNotEmpty() && skuBarcode.isNotEmpty() &&
                        stok.isNotEmpty() && kategori.isNotEmpty() &&
                        hargaBeli.isNotEmpty() && hargaJual.isNotEmpty()) {

                        if (selectedProduk != null) {
                            // Update produk
                            val updatedProduk = selectedProduk.copy(
                                namaProduk = namaProduk,
                                sku = skuBarcode,
                                stok = stok.toIntOrNull() ?: 0,
                                kategoriId = selectedKategoriId,
                                kategoriNama = kategori,
                                hargaBeli = hargaBeli.toDoubleOrNull() ?: 0.0,
                                hargaJual = hargaJual.toDoubleOrNull() ?: 0.0
                            )
                            viewModel.updateProduk(updatedProduk)
                            Toast.makeText(context, "Produk berhasil diupdate", Toast.LENGTH_SHORT).show()
                        } else {
                            // Tambah produk baru
                            viewModel.addProduk(
                                namaProduk = namaProduk,
                                sku = skuBarcode,
                                stok = stok.toIntOrNull() ?: 0,
                                kategoriId = selectedKategoriId,
                                kategoriNama = kategori,
                                hargaBeli = hargaBeli.toDoubleOrNull() ?: 0.0,
                                hargaJual = hargaJual.toDoubleOrNull() ?: 0.0
                            )
                            Toast.makeText(context, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Harap lengkapi semua field", Toast.LENGTH_SHORT).show()
                    }
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
}

@Composable
fun ProductCard(
    produk: ProdukEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val rupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }

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
                            text = produk.namaProduk,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = plusjakarta
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Status Badge
                        val badgeColor = when(produk.status) {
                            "Ready" -> Color(0xFF4CAF50)
                            "Low Stock" -> Color(0xFFFFA726)
                            else -> Color(0xFFEF5350)
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    color = badgeColor,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = produk.status,
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "SKU : ${produk.sku}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "STOK : ${produk.stok}",
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

                // Action Buttons
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = onEdit,
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
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price Row
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Harga Beli : ${rupiah.format(produk.hargaBeli)}",
                    fontSize = 14.sp,
                    color = Color(0xFFFDB913),
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusjakarta
                )
                Text(
                    text = "Harga Jual : ${rupiah.format(produk.hargaJual)}",
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
    KelolaProduk(navController = rememberNavController())

}