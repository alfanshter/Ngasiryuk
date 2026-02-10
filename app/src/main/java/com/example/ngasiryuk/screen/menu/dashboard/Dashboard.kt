package com.example.ngasiryuk.screen.menu.dashboard

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.AppScreen
import com.example.ngasiryuk.R
import com.example.ngasiryuk.di.AppContainer
import com.example.ngasiryuk.screen.component.dialog.ResetDatabaseDialog
import com.example.ngasiryuk.utils.DatabaseManager
import kotlinx.coroutines.launch
import java.io.File

data class MenuItem(
    val title: String,
    val iconRes: Int,  // Ubah ke Int untuk painterResource
    val route: String? = null,
    val onClickAction: String? = null  // Untuk action khusus seperti import, export, reset
)

@Composable
fun Dashboard(
    navController: NavHostController,
    viewModel: DashboardViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AppContainer.provideDashboardViewModel() as T
        }
    })
) {
    val toko by viewModel.toko.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State untuk dialog dan loading
    var showResetDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Launcher untuk memilih file database
    val importDatabaseLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            isLoading = true
            scope.launch {
                val success = DatabaseManager.importDatabase(context, it)
                isLoading = false
                if (success) {
                    // Restart activity untuk reload data
                    (context as? Activity)?.let { activity ->
                        Toast.makeText(context, "Aplikasi akan restart...", Toast.LENGTH_SHORT).show()
                        activity.finish()
                        activity.startActivity(activity.intent)
                    }
                }
            }
        }
    }

    // Reset Dialog
    if (showResetDialog) {
        ResetDatabaseDialog(
            onDismiss = { showResetDialog = false },
            onConfirm = {
                isLoading = true
                scope.launch {
                    val success = DatabaseManager.resetDatabase(context)
                    isLoading = false
                    if (success) {
                        // Restart activity untuk reload data
                        (context as? Activity)?.let { activity ->
                            Toast.makeText(context, "Aplikasi akan restart...", Toast.LENGTH_SHORT).show()
                            activity.finish()
                            activity.startActivity(activity.intent)
                        }
                    }
                }
            }
        )
    }

    // Data menu items dengan route navigation
    val menuItems = listOf(
        MenuItem(
            title = "Barang/Jasa",
            iconRes = R.drawable.barangjasa,
            route = AppScreen.KelolaProduk.route
        ),
        MenuItem(
            title = "Kategori",
            iconRes = R.drawable.kategori,
            route = AppScreen.ListKategori.route
        ),
        MenuItem(
            title = "Manajemen Stok",
            iconRes = R.drawable.manajemenstok,
            route = AppScreen.ManajemenStok.route
        ),
        MenuItem(
            title = "Kasir",
            iconRes = R.drawable.kasir,
            route = AppScreen.Kasir.route
        ),
        MenuItem(
            title = "Daftar Kasir",
            iconRes = R.drawable.kasir,
            route = AppScreen.DaftarKasir.route
        ),
        MenuItem(
            title = "Penjualan",
            iconRes = R.drawable.transaksi,
            route = AppScreen.RekapPenjualan.route
        ),
        MenuItem(
            title = "Pengaturan",
            iconRes = R.drawable.pengaturan,
            route = AppScreen.PengaturanPassword.route
        ),
        MenuItem(
            title = "Import Database",
            iconRes = R.drawable.importdb,
            route = null,
            onClickAction = "import"
        ),
        MenuItem(
            title = "Reset Database",
            iconRes = R.drawable.resetdb,
            route = null,
            onClickAction = "reset"
        ),
        MenuItem(
            title = "Manajemen Customer",
            iconRes = R.drawable.customer,
            route = AppScreen.ListCustomer.route
        ),
        MenuItem(
            title = "Export Database",
            iconRes = R.drawable.importdb,
            route = null,
            onClickAction = "export"
        )
    )

    Scaffold(
        topBar = {
            // Custom Top Bar dengan Rounded Bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(Color(0xFFFDB913))
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Title
                    Text(
                        text = "Selamat Datang",
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
        Box(modifier = Modifier.fillMaxSize()) {
            // Konten halaman
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5))
            ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    // Store Name Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Store Logo/Icon
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFDB913)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val currentToko = toko
                                    if (currentToko != null && currentToko.logoPath != null && File(currentToko.logoPath).exists()) {
                                        // Display uploaded logo
                                        AsyncImage(
                                            model = File(currentToko.logoPath),
                                            contentDescription = "Store Logo",
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        // Display default icon
                                        Image(
                                            painter = painterResource(id = R.drawable.icontoko2),
                                            contentDescription = "Store",
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = toko?.namaToko ?: "Nama Toko",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontFamily = plusjakarta
                                )
                            }

                            // Edit Icon
                            IconButton(
                                onClick = {
                                    navController.navigate(AppScreen.DaftarToko.route)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Menu Grid menggunakan items di LazyColumn
                    menuItems.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { menuItem ->
                                MenuCard(
                                    menuItem = menuItem,
                                    onClick = {
                                        when (menuItem.onClickAction) {
                                            "import" -> {
                                                // Launch file picker untuk import database
                                                importDatabaseLauncher.launch("*/*")
                                            }
                                            "export" -> {
                                                // Export database
                                                isLoading = true
                                                scope.launch {
                                                    val file = DatabaseManager.exportDatabase(context)
                                                    isLoading = false

                                                    // Share file atau buka file manager
                                                    file?.let { exportedFile ->
                                                        try {
                                                            val uri = FileProvider.getUriForFile(
                                                                context,
                                                                "${context.packageName}.fileprovider",
                                                                exportedFile
                                                            )
                                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                                setDataAndType(uri, "application/octet-stream")
                                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                            }
                                                            context.startActivity(Intent.createChooser(intent, "Open with"))
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                    }
                                                }
                                            }
                                            "reset" -> {
                                                // Show reset confirmation dialog
                                                showResetDialog = true
                                            }
                                            else -> {
                                                // Navigate to route
                                                menuItem.route?.let { route ->
                                                    navController.navigate(route)
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Jika jumlah item ganjil, tambahkan spacer
                            if (rowItems.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        // Loading overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFFFDB913))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Memproses...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
        }
    }
}

@Composable
fun MenuCard(
    menuItem: MenuItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize().padding(top = 21.dp, bottom = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Icon from drawable
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = menuItem.iconRes),
                    contentDescription = menuItem.title,
                    modifier = Modifier.size(50.dp)
                )
            }

            // Title
            Text(
                text = menuItem.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 3,
                textAlign = TextAlign.Center,
                fontFamily = plusjakarta
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardPreview() {
    Dashboard(navController = rememberNavController())
    
}