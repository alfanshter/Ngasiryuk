package com.example.ngasiryuk.screen.menu.kasir

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ngasiryuk.screen.component.dialog.SelectPrinterDialog
import com.example.ngasiryuk.screen.component.dialog.TambahCustomerDialog
import com.example.ngasiryuk.screen.component.dialog.TambahKasirDialog
import com.example.ngasiryuk.utils.MenuConstants
import com.example.ngasiryuk.utils.ThermalPrinterHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val selectedKasir by viewModel.selectedKasir.collectAsState()
    val selectedCustomer by viewModel.selectedCustomer.collectAsState()
    val keranjangItems by viewModel.keranjangItems.collectAsState()
    val totalTransaksi by viewModel.totalTransaksi.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val lastTransactionData by viewModel.lastTransactionData.collectAsState()

    // Context untuk Toast
    val context = androidx.compose.ui.platform.LocalContext.current

    // Printer helper
    val printerHelper = remember { ThermalPrinterHelper(context) }

    // Bluetooth permission launcher
    val bluetoothPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            android.widget.Toast.makeText(
                context,
                "Izin Bluetooth diberikan",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        } else {
            android.widget.Toast.makeText(
                context,
                "Izin Bluetooth diperlukan untuk mencetak struk",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    }

    // Request Bluetooth permissions saat pertama kali
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            )
        }
    }

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
    var showQRScanner by remember { mutableStateOf(false) }
    var showPembayaranBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // State untuk form tambah produk (langsung di halaman)
    var showDropdown by remember { mutableStateOf(false) }
    var selectedProduk by remember { mutableStateOf<com.example.ngasiryuk.data.local.entity.ProdukEntity?>(null) }
    var namaProduk by remember { mutableStateOf("") }
    var skuBarcode by remember { mutableStateOf("") }
    var hargaJual by remember { mutableStateOf("") }
    var jumlah by remember { mutableIntStateOf(1) }

    // State untuk menyimpan barcode dari scanner
    var scannedBarcode by remember { mutableStateOf("") }

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
            }.take(5)
        }
    }

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
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Form Tambah Produk (langsung di halaman)
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
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    cursorColor = Color.Black,
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
                                                    text = "Stok: ${produk.stok} | Rp ${produk.hargaJual.toInt()}",
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
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedProduk!!.stok > 0) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Found",
                                        tint = if (selectedProduk!!.stok > 0) Color(0xFF4CAF50) else Color(0xFFFF9800),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = if (selectedProduk!!.stok > 0) {
                                                "Produk ditemukan - Stok: ${selectedProduk!!.stok}"
                                            } else {
                                                "Produk ditemukan - Stok belum diinput"
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (selectedProduk!!.stok > 0) Color(0xFF2E7D32) else Color(0xFFE65100),
                                            fontFamily = plusjakarta
                                        )
                                        if (selectedProduk!!.stok == 0) {
                                            Text(
                                                text = "Tetap bisa dijual, update stok di Manajemen Stok",
                                                fontSize = 10.sp,
                                                color = Color(0xFFE65100),
                                                fontFamily = plusjakarta
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        // SKU/Kode Barcode dengan tombol scan (OPSIONAL)
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
                                        "SKU/Kode Barcode (Opsional)",
                                        color = Color.Gray,
                                        fontFamily = plusjakarta,
                                        fontSize = 14.sp
                                    )
                                },
                                modifier = Modifier.weight(1f),
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
                                    fontSize = 14.sp
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
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
                    }

                    item {
                        // Jumlah Section
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
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
                                        if (selectedProduk != null) {
                                            if (selectedProduk!!.stok > 0) {
                                                if (jumlah < selectedProduk!!.stok) jumlah++
                                            } else {
                                                jumlah++
                                            }
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

                    item {
                        // Tombol Simpan ke Keranjang
                        Button(
                            onClick = {
                                val isFormValid = namaProduk.isNotEmpty() && hargaJual.isNotEmpty()

                                if (isFormValid) {
                                    if (selectedProduk != null) {
                                        val produkCustom = selectedProduk!!.copy(
                                            hargaJual = hargaJual.toDoubleOrNull() ?: selectedProduk!!.hargaJual
                                        )
                                        viewModel.addToKeranjang(produkCustom, jumlah)
                                    } else {
                                        viewModel.addProduk(
                                            namaProduk,
                                            skuBarcode.ifEmpty { "-" },
                                            0,
                                            1,
                                            "Umum",
                                            0.0,
                                            hargaJual.toDoubleOrNull() ?: 0.0
                                        )

                                        val produkBaru = com.example.ngasiryuk.data.local.entity.ProdukEntity(
                                            id = 0,
                                            namaProduk = namaProduk,
                                            sku = skuBarcode.ifEmpty { "-" },
                                            stok = 0,
                                            kategoriId = 1,
                                            kategoriNama = "Umum",
                                            hargaBeli = 0.0,
                                            hargaJual = hargaJual.toDoubleOrNull() ?: 0.0
                                        )
                                        viewModel.addToKeranjang(produkBaru, jumlah)
                                    }

                                    // Reset form
                                    namaProduk = ""
                                    skuBarcode = ""
                                    hargaJual = ""
                                    jumlah = 1
                                    selectedProduk = null
                                    scannedBarcode = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFDB913)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = namaProduk.isNotEmpty() && hargaJual.isNotEmpty()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tambah ke Keranjang",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
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
                            onEdit = {
                                // Isi form dengan data dari keranjang item
                                namaProduk = item.nama
                                skuBarcode = item.sku
                                hargaJual = item.harga.toString()
                                jumlah = item.jumlah

                                // Cari produk asli dari database untuk set selectedProduk
                                val produkAsli = produkList.find { it.id == item.produkId }
                                selectedProduk = produkAsli

                                // Hapus item dari keranjang sementara
                                viewModel.removeFromKeranjang(index)
                            },
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

    // QR Scanner Screen
    if (showQRScanner) {
        QRScannerScreen(
            onBarcodeScanned = { barcode ->
                showQRScanner = false
                scannedBarcode = barcode
                val produk = viewModel.getProdukByBarcode(barcode)
                if (produk != null) {
                    selectedProduk = produk
                    namaProduk = produk.namaProduk
                    skuBarcode = produk.sku
                    hargaJual = produk.hargaJual.toInt().toString()
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
                },
                printerHelper = printerHelper,
                lastTransactionData = lastTransactionData,
                context = context
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PembayaranBottomSheetContent(
    totalTagihan: Int,
    onDismiss: () -> Unit,
    onSimpan: (diskon: Int, uangDibayarkan: Int, metodePembayaran: String, keterangan: String?) -> Unit,
    printerHelper: ThermalPrinterHelper,
    lastTransactionData: TransactionReceiptData?,
    context: android.content.Context
) {
    var diskon by remember { mutableStateOf("") }
    var uangDibayarkan by remember { mutableStateOf("") }
    var metodePembayaran by remember { mutableStateOf("Tunai") }
    var expandedMetode by remember { mutableStateOf(false) }
    var keterangan by remember { mutableStateOf("") }
    var showPrinterDialog by remember { mutableStateOf(false) }

    // Perhitungan
    val diskonValue = diskon.toIntOrNull() ?: 0
    val uangDibayarkanValue = uangDibayarkan.toIntOrNull() ?: 0
    val totalSetelahDiskon = totalTagihan - (totalTagihan * diskonValue / 100)
    val kembalian = uangDibayarkanValue - totalSetelahDiskon

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 700.dp)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Total Tagihan - lebih kompak
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TOTAL TAGIHAN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                fontFamily = plusjakarta,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Rp ${"%,d".format(totalTagihan)}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                fontFamily = plusjakarta
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(12.dp))

        // Uang Yang Dibayarkan dengan Input
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
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

        Spacer(modifier = Modifier.height(12.dp))

        // Kembalian Section - lebih kompak
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFFF9E6),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
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
                    modifier = Modifier.size(20.dp)
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

        Spacer(modifier = Modifier.height(12.dp))

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

        Spacer(modifier = Modifier.height(12.dp))

        // Keterangan - lebih kompak
        Text(
            text = "Keterangan (optional)",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontFamily = plusjakarta,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedTextField(
            value = keterangan,
            onValueChange = { keterangan = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
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
            minLines = 2,
            maxLines = 3
        )

        // Tombol Cetak Struk - selalu aktif, bisa cetak sebelum atau sesudah simpan
        OutlinedButton(
            onClick = {
                if (printerHelper.isBluetoothAvailable()) {
                    // Jika belum ada transaksi, validasi input dulu
                    if (lastTransactionData == null) {
                        if (uangDibayarkanValue < totalSetelahDiskon) {
                            android.widget.Toast.makeText(
                                context,
                                "Uang yang dibayarkan kurang dari total",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Simpan transaksi dulu, baru cetak
                            onSimpan(diskonValue, uangDibayarkanValue, metodePembayaran, keterangan.ifBlank { null })
                            // Tunggu sebentar untuk data tersimpan, lalu buka dialog printer
                            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                kotlinx.coroutines.delay(500) // Delay 500ms
                                showPrinterDialog = true
                            }
                        }
                    } else {
                        // Sudah ada transaksi, langsung cetak
                        showPrinterDialog = true
                    }
                } else {
                    android.widget.Toast.makeText(
                        context,
                        "Bluetooth tidak tersedia atau tidak aktif",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFFDB913)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color(0xFFFDB913)
            )
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Print",
                tint = Color(0xFFFDB913),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (lastTransactionData != null) "Cetak Struk" else "Simpan & Cetak Struk",
                color = Color(0xFFFDB913),
                fontWeight = FontWeight.SemiBold,
                fontFamily = plusjakarta,
                fontSize = 16.sp
            )
        }

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
                    .height(48.dp),
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
                    fontSize = 15.sp
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
                    .height(48.dp),
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
                    fontSize = 15.sp
                )
            }
        }

        // Padding bawah untuk scrolling yang lebih nyaman
        Spacer(modifier = Modifier.height(16.dp))
    }

    // Dialog Pilih Printer - di luar Column agar tidak ikut scroll
    if (showPrinterDialog) {
        val printers = printerHelper.getPairedPrinters()

        SelectPrinterDialog(
            printers = printers,
            onDismiss = { showPrinterDialog = false },
            onPrinterSelected = { printer ->
                showPrinterDialog = false
                // Cetak struk
                lastTransactionData?.let { data ->
                    printReceipt(
                        printerHelper = printerHelper,
                        printer = printer,
                        transactionData = data,
                        context = context
                    )
                }
            },
            onTestPrint = { printer ->
                printerHelper.testPrint(
                    printerDevice = printer,
                    onSuccess = {
                        android.widget.Toast.makeText(
                            context,
                            "Test print berhasil",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    },
                    onError = { error ->
                        android.widget.Toast.makeText(
                            context,
                            "Test print gagal: $error",
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        )
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Row pertama: Info Produk
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
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
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row kedua: Edit Button dan Quantity Controls (separated for better spacing)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Edit Button
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFE3F2FD),
                        contentColor = Color(0xFF2196F3)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF90CAF9)),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Edit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = plusjakarta,
                        color = Color(0xFF2196F3)
                    )
                }

                // Quantity Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(6.dp)
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

// Helper function untuk mencetak struk
private fun printReceipt(
    printerHelper: ThermalPrinterHelper,
    printer: BluetoothDevice,
    transactionData: TransactionReceiptData,
    context: android.content.Context
) {
    // Hardcoded info toko (bisa diganti dengan data dari database)
    val namaUsaha = "Ngasiryuk Store"
    val alamatUsaha = "Jl. Contoh No. 123"
    val teleponUsaha = "081234567890"

    // Generate nomor transaksi
    val localeID = Locale("in", "ID")
    val dateFormat = SimpleDateFormat("yyMMddHHmmss", localeID)
    val noTransaksi = "TRX-${dateFormat.format(Date())}"

    // Convert data
    val items = transactionData.items.map { item ->
        ThermalPrinterHelper.ReceiptItem(
            nama = item.nama,
            jumlah = item.jumlah,
            harga = item.harga
        )
    }

    printerHelper.printReceipt(
        printerDevice = printer,
        namaUsaha = namaUsaha,
        alamatUsaha = alamatUsaha,
        teleponUsaha = teleponUsaha,
        noTransaksi = noTransaksi,
        tanggal = Date(),
        namaKasir = transactionData.namaKasir,
        namaCustomer = transactionData.namaCustomer,
        items = items,
        subtotal = transactionData.subtotal,
        diskon = transactionData.diskon,
        total = transactionData.total,
        uangDibayar = transactionData.uangDibayar,
        kembalian = transactionData.kembalian,
        metodePembayaran = transactionData.metodePembayaran,
        keterangan = transactionData.keterangan,
        onSuccess = {
            android.widget.Toast.makeText(
                context,
                "Struk berhasil dicetak",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        },
        onError = { error ->
            android.widget.Toast.makeText(
                context,
                "Gagal mencetak struk: $error",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun KasirScreenPreview() {
    KasirScreen(navController = rememberNavController())

}