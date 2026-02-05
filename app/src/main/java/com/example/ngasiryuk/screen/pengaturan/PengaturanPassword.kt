package com.example.ngasiryuk.screen.pengaturan

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R
import com.example.ngasiryuk.screen.component.dialog.TambahPasswordDialog

@Composable
fun PengaturanPassword() {
    var showPasswordDialog by remember { mutableStateOf(false) }
    var menuStates by remember {
        mutableStateOf(
            mapOf(
                "Menu Barang Dan Jasa" to false,
                "Menu Kategori" to false,
                "Menu Manajemen Stok" to false,
                "Menu Kasir" to false,
                "Menu Rekap Penjualan" to false,
                "Menu Pengaturan" to false,
                "Menu Reset Database" to false,
                "Menu Manajemen Customer" to false,
                "Menu Export Database" to false
            )
        )
    }
    var selectedMenu by remember { mutableStateOf("") }

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
                        text = "Pengaturan",
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
            // Card Container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    item {


                        // Header
                        Text(
                            text = "Keamanan Menu",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = plusjakarta
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Pilih Menu Yang Ingin Dilindungi Dengan Password",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            fontFamily = plusjakarta
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Menu Items
                        MenuSecurityItem(
                            icon = painterResource(R.drawable.barangjasa), // ganti dengan icon yang sesuai
                            iconTint = Color.Unspecified,
                            title = "Menu Barang Dan Jasa",
                            isEnabled = menuStates["Menu Barang Dan Jasa"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Barang Dan Jasa"
                                    showPasswordDialog = true
                                } else {
                                    menuStates = menuStates.toMutableMap().apply {
                                        this["Menu Barang Dan Jasa"] = false
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MenuSecurityItem(
                            icon = painterResource(R.drawable.kategori),
                            iconTint = Color.Unspecified,
                            title = "Menu Kategori",
                            isEnabled = menuStates["Menu Kategori"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Kategori"
                                    showPasswordDialog = true
                                } else {
                                    menuStates = menuStates.toMutableMap().apply {
                                        this["Menu Kategori"] = false
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MenuSecurityItem(
                            icon = painterResource(R.drawable.manajemenstok),
                            iconTint = Color.Unspecified,
                            title = "Menu Manajemen Stok",
                            isEnabled = menuStates["Menu Manajemen Stok"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Manajemen Stok"
                                    showPasswordDialog = true
                                } else {
                                    menuStates = menuStates.toMutableMap().apply {
                                        this["Menu Manajemen Stok"] = false
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MenuSecurityItem(
                            icon = painterResource(R.drawable.kasir),
                            iconTint = Color.Unspecified,
                            title = "Menu Kasir",
                            isEnabled = menuStates["Menu Kasir"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Kasir"
                                    showPasswordDialog = true
                                } else {
                                    menuStates = menuStates.toMutableMap().apply {
                                        this["Menu Kasir"] = false
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MenuSecurityItem(
                            icon = painterResource(R.drawable.transaksi),
                            iconTint = Color.Unspecified,
                            title = "Menu Rekap Penjualan",
                            isEnabled = menuStates["Menu Rekap Penjualan"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Rekap Penjualan"
                                    showPasswordDialog = true
                                } else {
                                    menuStates = menuStates.toMutableMap().apply {
                                        this["Menu Rekap Penjualan"] = false
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MenuSecurityItem(
                            icon = painterResource(R.drawable.pengaturan),
                            iconTint = Color.Unspecified,
                            title = "Menu Pengaturan",
                            isEnabled = menuStates["Menu Pengaturan"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Pengaturan"
                                    showPasswordDialog = true
                                } else {
                                    menuStates = menuStates.toMutableMap().apply {
                                        this["Menu Pengaturan"] = false
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MenuSecurityItem(
                            icon = painterResource(R.drawable.resetdb),
                            iconTint = Color.Unspecified,
                            title = "Menu Reset Database",
                            isEnabled = menuStates["Menu Reset Database"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Reset Database"
                                    showPasswordDialog = true
                                } else {
                                    menuStates = menuStates.toMutableMap().apply {
                                        this["Menu Reset Database"] = false
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MenuSecurityItem(
                            icon = painterResource(R.drawable.customer),
                            iconTint = Color.Unspecified,
                            title = "Menu Manajemen Customer",
                            isEnabled = menuStates["Menu Manajemen Customer"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Manajemen Customer"
                                    showPasswordDialog = true
                                } else {
                                    menuStates = menuStates.toMutableMap().apply {
                                        this["Menu Manajemen Customer"] = false
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MenuSecurityItem(
                            icon = painterResource(R.drawable.exportdb),
                            iconTint = Color.Unspecified,
                            title = "Menu Export Database",
                            isEnabled = menuStates["Menu Export Database"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Export Database"
                                    showPasswordDialog = true
                                } else {
                                    menuStates = menuStates.toMutableMap().apply {
                                        this["Menu Export Database"] = false
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MenuSecurityItem(
                            icon = painterResource(R.drawable.importdb),
                            iconTint = Color.Unspecified,
                            title = "Menu Export Database",
                            isEnabled = menuStates["Menu Export Database"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Export Database"
                                    showPasswordDialog = true
                                } else {
                                    menuStates = menuStates.toMutableMap().apply {
                                        this["Menu Export Database"] = false
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog Tambah Password
    if (showPasswordDialog) {
        TambahPasswordDialog(
            onDismiss = {
                showPasswordDialog = false
                // Reset switch jika dibatalkan
            },
            onSave = { password ->
                // Handle save password untuk menu yang dipilih
                menuStates = menuStates.toMutableMap().apply {
                    this[selectedMenu] = true
                }
                showPasswordDialog = false
            }
        )
    }
}

@Composable
fun MenuSecurityItem(
    icon: Painter,
    iconTint: Color,
    title: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = iconTint.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = plusjakarta, fontWeight = FontWeight.Medium
            )
        }

        // Switch
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFFFDB913),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}



@Preview(showBackground = true)
@Composable
private fun PengaturanPasswordPreview() {
    PengaturanPassword()

}