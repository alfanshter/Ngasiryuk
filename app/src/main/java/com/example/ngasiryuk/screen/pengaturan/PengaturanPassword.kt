package com.example.ngasiryuk.screen.pengaturan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R
import com.example.ngasiryuk.di.AppContainer
import com.example.ngasiryuk.screen.component.PasswordProtectedScreen
import com.example.ngasiryuk.screen.component.dialog.TambahPasswordDialog
import com.example.ngasiryuk.utils.MenuConstants

@Composable
fun PengaturanPassword(
    navController: NavController
) {
    val passwordViewModel = remember {
        AppContainer.provideMenuPasswordViewModel()
    }

    PasswordProtectedScreen(
        menuName = MenuConstants.MENU_PENGATURAN,
        viewModel = passwordViewModel,
        onAccessGranted = {
            PengaturanPasswordContent(navController)
        },
        onAccessDenied = {
            navController.popBackStack()
        }
    )
}

@Composable
private fun PengaturanPasswordContent(
    navController: NavController
) {
    // Get viewModel from AppContainer with error handling
    val viewModel = remember {
        try {
            AppContainer.provideMenuPasswordViewModel()
        } catch (e: Exception) {
            android.util.Log.e("PengaturanPassword", "Error creating ViewModel", e)
            null
        }
    }

    // Show error if ViewModel creation failed
    if (viewModel == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error: Tidak dapat memuat pengaturan password",
                color = Color.Red,
                fontFamily = plusjakarta
            )
        }
        return
    }

    var showPasswordDialog by remember { mutableStateOf(false) }
    var selectedMenu by remember { mutableStateOf("") }

    val menuPasswords by viewModel.menuPasswords.collectAsState()

    // Map menu states dari database
    val menuStates = remember(menuPasswords) {
        mapOf(
            "Menu Barang Dan Jasa" to (menuPasswords["Menu Barang Dan Jasa"]?.isEnabled ?: false),
            "Menu Kategori" to (menuPasswords["Menu Kategori"]?.isEnabled ?: false),
            "Menu Manajemen Stok" to (menuPasswords["Menu Manajemen Stok"]?.isEnabled ?: false),
            "Menu Kasir" to (menuPasswords["Menu Kasir"]?.isEnabled ?: false),
            "Menu Rekap Penjualan" to (menuPasswords["Menu Rekap Penjualan"]?.isEnabled ?: false),
            "Menu Pengaturan" to (menuPasswords["Menu Pengaturan"]?.isEnabled ?: false),
            "Menu Reset Database" to (menuPasswords["Menu Reset Database"]?.isEnabled ?: false),
            "Menu Manajemen Customer" to (menuPasswords["Menu Manajemen Customer"]?.isEnabled ?: false),
            "Menu Export Database" to (menuPasswords["Menu Export Database"]?.isEnabled ?: false),
            "Menu Import Database" to (menuPasswords["Menu Import Database"]?.isEnabled ?: false)
        )
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
                                    viewModel.deleteMenuPassword("Menu Barang Dan Jasa")
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
                                    viewModel.deleteMenuPassword("Menu Kategori")
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
                                    viewModel.deleteMenuPassword("Menu Manajemen Stok")
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
                                    viewModel.deleteMenuPassword("Menu Kasir")
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
                                    viewModel.deleteMenuPassword("Menu Rekap Penjualan")
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
                                    viewModel.deleteMenuPassword("Menu Pengaturan")
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
                                    viewModel.deleteMenuPassword("Menu Reset Database")
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
                                    viewModel.deleteMenuPassword("Menu Manajemen Customer")
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
                                    viewModel.deleteMenuPassword("Menu Export Database")
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MenuSecurityItem(
                            icon = painterResource(R.drawable.importdb),
                            iconTint = Color.Unspecified,
                            title = "Menu Import Database",
                            isEnabled = menuStates["Menu Import Database"] ?: false,
                            onToggle = { enabled ->
                                if (enabled) {
                                    selectedMenu = "Menu Import Database"
                                    showPasswordDialog = true
                                } else {
                                    viewModel.deleteMenuPassword("Menu Import Database")
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
            },
            onSave = { password ->
                viewModel.saveMenuPassword(selectedMenu, password)
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
    PengaturanPassword(navController = rememberNavController())

}