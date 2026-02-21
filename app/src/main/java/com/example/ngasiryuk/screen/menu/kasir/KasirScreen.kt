package com.example.ngasiryuk.screen.menu.kasir

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R
import com.example.ngasiryuk.di.AppContainer
import com.example.ngasiryuk.screen.component.PasswordProtectedScreen
import com.example.ngasiryuk.screen.component.dialog.TambahCustomerDialog
import com.example.ngasiryuk.screen.component.dialog.TambahKasirDialog
import com.example.ngasiryuk.screen.component.bottomsheet.TambahKeranjangBottomSheet
import com.example.ngasiryuk.utils.MenuConstants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KasirScreen(
    navController: NavController,
    viewModel: KasirViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AppContainer.provideKasirViewModel() as T
        }
    })
) {
    val passwordViewModel = remember {
        AppContainer.provideMenuPasswordViewModel()
    }

    PasswordProtectedScreen(
        menuName = MenuConstants.MENU_KASIR,
        viewModel = passwordViewModel,
        onAccessGranted = {
            KasirScreenContent(navController, viewModel)
        },
        onAccessDenied = {
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KasirScreenContent(
    navController: NavController,
    viewModel: KasirViewModel
) {
    // State dari ViewModel
    val kasirList by viewModel.kasirList.collectAsState()
    val customerList by viewModel.customerList.collectAsState()
    val produkList by viewModel.produkList.collectAsState()
    val kategoriList by viewModel.kategoriList.collectAsState()
    val selectedKasir by viewModel.selectedKasir.collectAsState()
    val selectedCustomer by viewModel.selectedCustomer.collectAsState()
    val keranjangItems by viewModel.keranjangItems.collectAsState()
    val totalTransaksi by viewModel.totalTransaksi.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Context untuk Toast
    val context = androidx.compose.ui.platform.LocalContext.current

    // Handle success message dengan Toast
    LaunchedEffect(successMessage) {
        successMessage?.let { message ->
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show()
            viewModel.clearSuccess()
        }
    }

    // Handle error message dengan Toast
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // Local state for UI
    var expandedKasir by remember { mutableStateOf(false) }
    var expandedPelanggan by remember { mutableStateOf(false) }
    var showTambahCustomerDialog by remember { mutableStateOf(false) }
    var showTambahKasirDialog by remember { mutableStateOf(false) }
    var showTambahKeranjangBottomSheet by remember { mutableStateOf(false) }
    var showQRScannerInBottomSheet by remember { mutableStateOf(false) }
    var showQRScanner by remember { mutableStateOf(false) }
    var showPembayaranBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val keranjangSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // State untuk menyimpan barcode dari scanner
    var scannedBarcode by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            // Custom Top Bar dengan Rounded Bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(Color(0xFFFDB913))
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
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
                        text = "Kasir",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = plusjakarta
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Card Container dengan LazyColumn
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 0.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    item {
                        // Label Kasir
                        Text(
                            text = "Kasir",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            fontFamily = plusjakarta,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    item {
                        // Dropdown Kasir
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ExposedDropdownMenuBox(
                                expanded = expandedKasir,
                                onExpandedChange = { expandedKasir = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedKasir?.namaKasir ?: "Pilih Kasir",
                                    onValueChange = {},
                                    readOnly = true,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Person",
                                            tint = Color.Gray
                                        )
                                    },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKasir)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                        cursorColor = Color.Black,
                                        unfocusedBorderColor = Color(0xFFE0E0E0),
                                        focusedBorderColor = Color(0xFFFDB913),
                                        unfocusedContainerColor = Color(0xFFF5F5F5),
                                        focusedContainerColor = Color(0xFFF5F5F5)
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedKasir,
                                    onDismissRequest = { expandedKasir = false }
                                ) {
                                    kasirList.forEach { kasir ->
                                        DropdownMenuItem(
                                            text = { Text(kasir.namaKasir) },
                                            onClick = {
                                                viewModel.selectKasir(kasir)
                                                expandedKasir = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Tombol Tambah Kasir
                            IconButton(
                                onClick = { showTambahKasirDialog = true },
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        color = Color(0xFFFDB913),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    item {
                        // Label Pelanggan
                        Text(
                            text = "Pelanggan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            fontFamily = plusjakarta,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    item {
                        // Dropdown Pelanggan
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ExposedDropdownMenuBox(
                                expanded = expandedPelanggan,
                                onExpandedChange = { expandedPelanggan = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedCustomer?.nama ?: "Pilih Pelanggan (Opsional)",
                                    onValueChange = {},
                                    readOnly = true,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Person",
                                            tint = Color.Gray
                                        )
                                    },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPelanggan)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                        cursorColor = Color.Black,
                                        unfocusedBorderColor = Color(0xFFE0E0E0),
                                        focusedBorderColor = Color(0xFFFDB913),
                                        unfocusedContainerColor = Color(0xFFF5F5F5),
                                        focusedContainerColor = Color(0xFFF5F5F5)
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedPelanggan,
                                    onDismissRequest = { expandedPelanggan = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Tanpa Pelanggan") },
                                        onClick = {
                                            viewModel.selectCustomer(null)
                                            expandedPelanggan = false
                                        }
                                    )
                                    customerList.forEach { customer ->
                                        DropdownMenuItem(
                                            text = { Text(customer.nama) },
                                            onClick = {
                                                viewModel.selectCustomer(customer)
                                                expandedPelanggan = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = { showTambahCustomerDialog = true },
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        color = Color(0xFFFDB913),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    item {
                        // Total Transaksi
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Transaksi",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontFamily = plusjakarta
                                )
                                Text(
                                    text = "Rp ${"%,d".format(totalTransaksi)}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF01A14E),
                                    fontFamily = plusjakarta
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.clearKeranjang()
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = Color(0xFFFFEBEE),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Reset",
                                    tint = Color(0xFFFF5252),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    item {
                        // Tombol Scan Produk
                        Button(
                            onClick = { showQRScanner = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFDB913)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.qrcode),
                                contentDescription = "Scan",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Scan Produk",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontFamily = plusjakarta,
                                fontSize = 16.sp
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        // Tombol Tambah Keranjang
                        OutlinedButton(
                            onClick = { showTambahKeranjangBottomSheet = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(0xFFF5F5F5)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tambah Keranjang",
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium,
                                fontFamily = plusjakarta,
                                fontSize = 16.sp
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        // Header Keranjang
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Keranjang",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontFamily = plusjakarta
                            )
                            Text(
                                text = "${keranjangItems.size} Item",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFFDB913),
                                fontFamily = plusjakarta
                            )
                        }
                    }

                    item {
                        // Divider
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFE0E0E0))
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // List Keranjang Items
                    items(keranjangItems.size) { index ->
                        val item = keranjangItems[index]
                        KeranjangItemCard(
                            item = item,
                            onEdit = { /* Handle edit */ },
                            onPlus = {
                                viewModel.updateKeranjangItemJumlah(index, item.jumlah + 1)
                            },
                            onMinus = {
                                if (item.jumlah > 1) {
                                    viewModel.updateKeranjangItemJumlah(index, item.jumlah - 1)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        // Tombol Bayar
                        Button(
                            onClick = { showPembayaranBottomSheet = true },
                            modifier = Modifier
                                .fillMaxWidth().padding(bottom = 35.dp)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.bayar),
                                contentDescription = "Payment",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BAYAR",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = plusjakarta,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog Tambah Customer
    if (showTambahCustomerDialog) {
        TambahCustomerDialog(
            onDismiss = { showTambahCustomerDialog = false },
            onSave = { nama, nomerWa, alamat ->
                viewModel.addCustomer(nama, nomerWa, alamat)
                showTambahCustomerDialog = false
            }
        )
    }

    // Dialog Tambah Kasir
    if (showTambahKasirDialog) {
        TambahKasirDialog(
            onDismiss = { showTambahKasirDialog = false },
            onSave = { namaKasir ->
                viewModel.addKasir(namaKasir)
                showTambahKasirDialog = false
            }
        )
    }

    // BottomSheet Tambah Ke Keranjang
    if (showTambahKeranjangBottomSheet && !showQRScannerInBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTambahKeranjangBottomSheet = false },
            sheetState = keranjangSheetState,
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
            TambahKeranjangBottomSheet(
                produkList = produkList,
                kategoriList = kategoriList,
                scannedBarcode = scannedBarcode,
                onDismiss = {
                    showTambahKeranjangBottomSheet = false
                    scannedBarcode = "" // Reset barcode
                },
                onSave = { produk, jumlah ->
                    viewModel.addToKeranjang(produk, jumlah)
                    showTambahKeranjangBottomSheet = false
                    scannedBarcode = "" // Reset barcode
                },
                onAddProduk = { namaProduk, sku, stok, kategoriId, kategoriNama, hargaBeli, hargaJual ->
                    viewModel.addProduk(namaProduk, sku, stok, kategoriId, kategoriNama, hargaBeli, hargaJual)
                },
                onScanBarcode = {
                    showQRScannerInBottomSheet = true
                }
            )
        }
    }

    // QR Scanner dalam BottomSheet
    if (showQRScannerInBottomSheet) {
        QRScannerScreen(
            onBarcodeScanned = { barcode ->
                scannedBarcode = barcode
                showQRScannerInBottomSheet = false
            },
            onDismiss = { showQRScannerInBottomSheet = false }
        )
    }

    // QR Scanner Screen
    if (showQRScanner) {
        QRScannerScreen(
            onBarcodeScanned = { barcode ->
                showQRScanner = false
                val produk = viewModel.getProdukByBarcode(barcode)
                if (produk != null) {
                    viewModel.addToKeranjang(produk, 1)
                    // TODO: Bisa tambahkan snackbar/toast untuk notifikasi sukses
                } else {
                    // TODO: Bisa tambahkan snackbar/toast untuk notifikasi produk tidak ditemukan
                }
            },
            onDismiss = { showQRScanner = false }
        )
    }

    // BottomSheet Pembayaran
    if (showPembayaranBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPembayaranBottomSheet = false },
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
            PembayaranBottomSheetContent(
                totalTagihan = totalTransaksi,
                onDismiss = { showPembayaranBottomSheet = false },
                onSimpan = { diskon, uangDibayarkan, metodePembayaran, keterangan ->
                    // Handle simpan pembayaran
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                        val success = viewModel.simpanTransaksi(
                            diskon = diskon,
                            uangDibayarkan = uangDibayarkan,
                            metodePembayaran = metodePembayaran,
                            keterangan = keterangan
                        )
                        if (success) {
                            showPembayaranBottomSheet = false
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PembayaranBottomSheetContent(
    totalTagihan: Int,
    onDismiss: () -> Unit,
    onSimpan: (diskon: Int, uangDibayarkan: Int, metodePembayaran: String, keterangan: String?) -> Unit
) {
    var diskon by remember { mutableStateOf("") }
    var uangDibayarkan by remember { mutableStateOf("") }
    var metodePembayaran by remember { mutableStateOf("Tunai") }
    var expandedMetode by remember { mutableStateOf(false) }
    var keterangan by remember { mutableStateOf("") }

    // Perhitungan
    val diskonValue = diskon.toIntOrNull() ?: 0
    val uangDibayarkanValue = uangDibayarkan.toIntOrNull() ?: 0
    val totalSetelahDiskon = totalTagihan - (totalTagihan * diskonValue / 100)
    val kembalian = uangDibayarkanValue - totalSetelahDiskon

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 16.dp)
    ) {
        // Total Tagihan
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TOTAL TAGIHAN",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                fontFamily = plusjakarta,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rp ${"%,d".format(totalTagihan)}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                fontFamily = plusjakarta
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Diskon Section dengan Input
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        {
            Text(
                text = "Diskon",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = plusjakarta,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = diskon,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        diskon = it
                    }
                },
                placeholder = {
                    Text(
                        "0",
                        color = Color.Gray,
                        fontFamily = plusjakarta,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFFFDB913),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5)
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusjakarta
                ),
                singleLine = true
            )
        }

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFE0E0E0))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Uang Yang Dibayarkan dengan Input
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        {
            Text(
                text = "Uang Yang Dibayarkan",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = plusjakarta,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = uangDibayarkan,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        uangDibayarkan = it
                    }
                },
                placeholder = {
                    Text(
                        "Rp 0",
                        color = Color.Gray,
                        fontFamily = plusjakarta,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFFFDB913),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5)
                ),
                leadingIcon = {
                    Text(
                        "Rp",
                        color = Color.Gray,
                        fontFamily = plusjakarta,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusjakarta
                ),
                singleLine = true
            )
        }

        // Kembalian Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFFF9E6),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.bayar),
                    contentDescription = "Kembalian",
                    tint = Color(0xFFFBC020),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kembalian",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = plusjakarta
                )
            }
            Text(
                text = "Rp ${if (kembalian >= 0) "%,d".format(kembalian) else "0"}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = plusjakarta
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Metode Pembayaran
        Text(
            text = "Metode Pembayaran",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = plusjakarta,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expandedMetode,
            onExpandedChange = { expandedMetode = it }
        ) {
            OutlinedTextField(
                value = metodePembayaran,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMetode)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFFFDB913),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5)
                )
            )
            ExposedDropdownMenu(
                expanded = expandedMetode,
                onDismissRequest = { expandedMetode = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Tunai") },
                    onClick = {
                        metodePembayaran = "Tunai"
                        expandedMetode = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Transfer") },
                    onClick = {
                        metodePembayaran = "Transfer"
                        expandedMetode = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("QRIS") },
                    onClick = {
                        metodePembayaran = "QRIS"
                        expandedMetode = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Keterangan
        Text(
            text = "Keterangan (optional)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = plusjakarta,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = keterangan,
            onValueChange = { keterangan = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFFFDB913),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedContainerColor = Color(0xFFF5F5F5)
            ),
            minLines = 3,
            maxLines = 4
        )

        // Buttons
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
                    onSimpan(
                        diskonValue,
                        uangDibayarkanValue,
                        metodePembayaran,
                        keterangan.ifBlank { null }
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFDB913)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = uangDibayarkanValue >= totalSetelahDiskon
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
fun KeranjangItemCard(
    item: KeranjangItem,
    onEdit: () -> Unit,
    onPlus: () -> Unit,
    onMinus: () -> Unit
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
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Info Produk
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.nama,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "SKU : ${item.sku}",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row {
                    Text(
                        text = "1 x Rp ${"%,d".format(item.harga)}",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )
                    Text(
                        text = " = ",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )
                    Text(
                        text = "Rp ${"%,d".format(item.harga * item.jumlah)}",
                        fontSize = 11.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold,
                        fontFamily = plusjakarta
                    )
                }
            }

            // Edit Button dan Quantity Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Quantity Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(4.dp)
                ) {
                    // Plus Button
                    IconButton(
                        onClick = onPlus,
                        modifier = Modifier
                            .size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Plus",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Jumlah
                    Text(
                        text = item.jumlah.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = plusjakarta,
                        modifier = Modifier.widthIn(min = 20.dp),
                        textAlign = TextAlign.Center
                    )

                    // Minus Button
                    IconButton(
                        onClick = onMinus,
                        modifier = Modifier
                            .size(28.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.minus),
                            contentDescription = "Minus",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KasirScreenPreview() {
    KasirScreen(navController = rememberNavController())

}