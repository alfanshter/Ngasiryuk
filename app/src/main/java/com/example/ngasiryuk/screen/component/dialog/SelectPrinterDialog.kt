package com.example.ngasiryuk.screen.component.dialog

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.galonqu.commond.plusjakarta

@Composable
fun SelectPrinterDialog(
    printers: List<BluetoothDevice>,
    onDismiss: () -> Unit,
    onPrinterSelected: (BluetoothDevice) -> Unit,
    onTestPrint: (BluetoothDevice) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pilih Printer",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = plusjakarta
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (printers.isEmpty()) {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "No Printer",
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFE0E0E0)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Tidak ada printer",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            fontFamily = plusjakarta
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Pastikan Bluetooth aktif dan\nprinter sudah dipasangkan",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = plusjakarta,
                            lineHeight = 20.sp
                        )
                    }
                } else {
                    // Printer list
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(printers) { printer ->
                            PrinterItem(
                                printer = printer,
                                onSelect = { onPrinterSelected(printer) },
                                onTestPrint = { onTestPrint(printer) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Close button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Tutup",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = plusjakarta,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PrinterItem(
    printer: BluetoothDevice,
    onSelect: () -> Unit,
    onTestPrint: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Bluetooth",
                    tint = Color(0xFFFDB913),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    @Suppress("MissingPermission")
                    Text(
                        text = printer.name ?: "Unknown Device",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        fontFamily = plusjakarta
                    )
                    Text(
                        text = printer.address,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )
                }
            }

            IconButton(
                onClick = onTestPrint,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Test Print",
                    tint = Color(0xFFFDB913)
                )
            }
        }
    }
}





