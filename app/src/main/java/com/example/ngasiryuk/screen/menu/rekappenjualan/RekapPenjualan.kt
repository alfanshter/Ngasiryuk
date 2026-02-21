package com.example.ngasiryuk.screen.menu.rekappenjualan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.example.ngasiryuk.utils.MenuConstants
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RekapPenjualan(
    navController: NavController,
    viewModel: RekapPenjualanViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AppContainer.provideRekapPenjualanViewModel() as T
        }
    })
) {
    val passwordViewModel = remember {
        AppContainer.provideMenuPasswordViewModel()
    }

    PasswordProtectedScreen(
        menuName = MenuConstants.MENU_REKAP_PENJUALAN,
        viewModel = passwordViewModel,
        onAccessGranted = {
            RekapPenjualanContent(navController, viewModel)
        },
        onAccessDenied = {
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RekapPenjualanContent(
    navController: NavController,
    viewModel: RekapPenjualanViewModel
) {
    // State dari ViewModel
    val selectedDateCalendar by viewModel.selectedDate.collectAsState()
    val transaksiList by viewModel.transaksiList.collectAsState()
    val pemasukan by viewModel.totalPemasukan.collectAsState()
    val pengeluaran by viewModel.totalPengeluaran.collectAsState()
    val totalLabaBersih by viewModel.totalLabaBersih.collectAsState()
    val pertumbuhanPersen by viewModel.pertumbuhanPersen.collectAsState()
    val jumlahTransaksi by viewModel.jumlahTransaksi.collectAsState()

    // Format tanggal untuk tampilan
    var selectedDate by remember {
        mutableStateOf(
            String.format(
                Locale.getDefault(),
                "%02d-%02d-%04d",
                selectedDateCalendar.get(Calendar.DAY_OF_MONTH),
                selectedDateCalendar.get(Calendar.MONTH) + 1,
                selectedDateCalendar.get(Calendar.YEAR)
            )
        )
    }
    var showDatePicker by remember { mutableStateOf(false) }

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
                            text = "Rekap Penjualan",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = plusjakarta
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Date Picker Field
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {},
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.calender),
                                contentDescription = "Calendar",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable { showDatePicker = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            unfocusedBorderColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            focusedLabelColor = Color.Gray,
                            disabledBorderColor = Color.White,
                            disabledContainerColor = Color.White,
                            disabledLabelColor = Color.Gray,
                            disabledLeadingIconColor = Color.Gray,
                            disabledTrailingIconColor = Color.Gray
                        ),
                        enabled = false,
                        interactionSource = remember { MutableInteractionSource() }
                            .also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            showDatePicker = true
                                        }
                                    }
                                }
                            }
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        // Konten halaman
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            item {
                // Row Pemasukan & Pengeluaran
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card Pemasukan
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.pemasukan),
                                    contentDescription = "Pemasukan",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Pemasukan",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontFamily = plusjakarta
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Rp ${"%,d".format(pemasukan)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50),
                                fontFamily = plusjakarta
                            )
                        }
                    }

                    // Card Pengeluaran
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.pengeluaran),
                                    contentDescription = "Pengeluaran",
                                    tint = Color(0xFFFF5252),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Pengeluaran",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontFamily = plusjakarta
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Rp ${"%,d".format(pengeluaran)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF5252),
                                fontFamily = plusjakarta
                            )
                        }
                    }
                }

                // Card Total Laba Bersih
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFDB913))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Total Laba Bersih",
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontFamily = plusjakarta
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Rp ${"%,d".format(totalLabaBersih)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = plusjakarta
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tumbuh $pertumbuhanPersen% dari Kemarin",
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.7f),
                            fontFamily = plusjakarta
                        )
                    }
                }

                // Total Penjualan Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Penjualan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = plusjakarta
                    )
                    Text(
                        text = "$jumlahTransaksi Transaksi",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )
                }

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE0E0E0))
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Header Daftar Penjualan
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daftar Penjualan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = plusjakarta
                    )
                    Text(
                        text = "Lihat Semua",
                        fontSize = 14.sp,
                        color = Color(0xFFFDB913),
                        fontWeight = FontWeight.Medium,
                        fontFamily = plusjakarta
                    )
                }
            }

            // List Penjualan
            items(transaksiList.size) { index ->
                TransaksiCard(
                    transaksi = transaksiList[index]
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    // DatePicker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis: Long ->
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = millis
                            selectedDate = String.format(
                                Locale.getDefault(),
                                "%02d-%02d-%04d",
                                calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.MONTH) + 1,
                                calendar.get(Calendar.YEAR)
                            )
                            viewModel.setSelectedDate(calendar)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(
                        "OK",
                        color = Color(0xFFFDB913),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text(
                        "Batal",
                        color = Color.Gray
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Color(0xFFFDB913),
                    todayDateBorderColor = Color(0xFFFDB913),
                    todayContentColor = Color(0xFFFDB913)
                )
            )
        }
    }
}

@Composable
fun TransaksiCard(transaksi: com.example.ngasiryuk.data.local.entity.TransaksiEntity) {
    // Load detail produk dari transaksi
    var detailList by remember { mutableStateOf<List<com.example.ngasiryuk.data.local.entity.DetailTransaksiEntity>>(emptyList()) }
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(transaksi.id) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            val dao = com.example.ngasiryuk.data.local.database.AppDatabase.getDatabase(context).detailTransaksiDao()
            dao.getDetailByTransaksiId(transaksi.id).collect { details ->
                detailList = details
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header dengan metode pembayaran
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Kasir: ${transaksi.namaKasir}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )
                    if (transaksi.namaCustomer != null) {
                        Text(
                            text = "Pelanggan: ${transaksi.namaCustomer}",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontFamily = plusjakarta
                        )
                    }
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = transaksi.metodePembayaran,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4CAF50),
                        fontFamily = plusjakarta
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Garis pembatas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFE0E0E0))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Daftar Produk
            if (detailList.isNotEmpty()) {
                Text(
                    text = "Produk:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(6.dp))

                detailList.take(3).forEach { detail ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${detail.jumlah}x ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                fontFamily = plusjakarta
                            )
                            Text(
                                text = detail.namaProduk,
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontFamily = plusjakarta,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Text(
                            text = "Rp ${"%,d".format(detail.subtotal)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50),
                            fontFamily = plusjakarta
                        )
                    }
                }

                if (detailList.size > 3) {
                    Text(
                        text = "+${detailList.size - 3} produk lainnya",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Info transaksi
            if (transaksi.diskon > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Diskon (${transaksi.diskon}%)",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )
                    Text(
                        text = "- Rp ${"%,d".format(transaksi.totalBelanja - transaksi.totalSetelahDiskon)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF5252),
                        fontFamily = plusjakarta
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Garis pembatas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFE0E0E0))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Total akhir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Dibayar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta
                )
                Text(
                    text = "Rp ${"%,d".format(transaksi.totalSetelahDiskon)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50),
                    fontFamily = plusjakarta
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RekapPenjualanPreview() {
    RekapPenjualan(navController = rememberNavController())
}

