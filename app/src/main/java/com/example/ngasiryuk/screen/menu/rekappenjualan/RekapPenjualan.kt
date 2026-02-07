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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RekapPenjualan(navController: NavController) {
    var selectedDate by remember { mutableStateOf("12-09-2026") }
    var showDatePicker by remember { mutableStateOf(false) }

    val pemasukan = 120000
    val pengeluaran = 120000
    val totalLabaBersih = 300000
    val pertumbuhanPersen = 12

    val daftarPenjualan = listOf(
        PenjualanItem("Autan liquid", 3000, true),
        PenjualanItem("Autan liquid", 3000, true),
        PenjualanItem("Autan liquid", 3000, true)
    )

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
                            onClick = {navController.popBackStack() },
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
                        text = "${daftarPenjualan.size} Transaksi",
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
            items(daftarPenjualan.size) { index ->
                PenjualanCard(
                    item = daftarPenjualan[index]
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
                        datePickerState.selectedDateMillis?.let { millis ->
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = millis
                            selectedDate = String.format(
                                "%02d-%02d-%04d",
                                calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.MONTH) + 1,
                                calendar.get(Calendar.YEAR)
                            )
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

// Data class untuk item penjualan
data class PenjualanItem(
    val nama: String,
    val total: Int,
    val terjual: Boolean
)

@Composable
fun PenjualanCard(item: PenjualanItem) {
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
            // Info Barang
            Column {
                Text(
                    text = item.nama,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Total = Rp ${"%,d".format(item.total)}",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
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
                    text = "Terjual",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
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