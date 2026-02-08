package com.example.ngasiryuk.screen.menu.daftartoko

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.AppScreen
import com.example.ngasiryuk.R
import com.example.ngasiryuk.di.AppContainer
import com.example.ngasiryuk.utils.ImageUtils
import com.example.ngasiryuk.utils.rememberImagePermission
import java.io.File

@Composable
fun DaftarToko(
    navController: NavController,
    viewModel: DaftarTokoViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AppContainer.provideDaftarTokoViewModel() as T
        }
    })
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Permission handling
    val (hasPermission, requestPermission) = rememberImagePermission(
        onPermissionGranted = {},
        onPermissionDenied = {
            // Show snackbar when permission denied
        }
    )

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Save image to internal storage
            val savedPath = ImageUtils.saveImageToInternalStorage(
                context = context,
                uri = it,
                fileName = "logo_toko"
            )
            savedPath?.let { path ->
                viewModel.onEvent(DaftarTokoEvent.OnLogoSelected(path))
            }
        }
    }

    // Handle navigation when save is successful
    LaunchedEffect(state.isSaveSuccess) {
        if (state.isSaveSuccess) {
            navController.navigate(AppScreen.Dashboard.route) {
                popUpTo(AppScreen.DaftarToko.route) { inclusive = true }
            }
            viewModel.resetSaveSuccess()
        }
    }

    // Show error message
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        text = "Profil Toko",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black, fontFamily = plusjakarta
                    )
                }
            }
        }, contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        // Konten halaman
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp)
                )
                {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Upload Logo Section
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clickable {
                                        // Check permission first
                                        if (hasPermission) {
                                            imagePickerLauncher.launch("image/*")
                                        } else {
                                            requestPermission()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                // Background Circle with image
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE8E8E8)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (state.logoPath != null && File(state.logoPath).exists()) {
                                        // Show selected image
                                        AsyncImage(
                                            model = File(state.logoPath),
                                            contentDescription = "Logo Toko",
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        // Show default icon
                                        Image(
                                            painter = painterResource(R.drawable.icontoko),
                                            contentDescription = "Store Icon",
                                            modifier = Modifier.size(48.dp),
                                        )
                                    }
                                }

                                // Camera Button
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .align(Alignment.BottomEnd)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFDB913))
                                        .border(2.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddCircle,
                                        contentDescription = "Upload",
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.Black
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Upload Logo Toko",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black, fontFamily = plusjakarta
                            )

                            Text(
                                text = "Pastikan Format jpg, Png",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontFamily = plusjakarta,
                                fontWeight = FontWeight.Normal
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Nama Toko
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Nama Toko",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium, fontFamily = plusjakarta,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = state.namaToko,
                                    onValueChange = { viewModel.onEvent(DaftarTokoEvent.OnNamaTokoChange(it)) },
                                    placeholder = {
                                        Text(
                                            text = "Contoh : Galonku",
                                            color = Color.Gray
                                        )
                                    },
                                    leadingIcon = {
                                        Image(
                                            painter = painterResource(R.drawable.icontoko),
                                            contentDescription = "Store",
                                            Modifier.size(24.dp)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = Color(0xFFF5F5F5),
                                        focusedContainerColor = Color(0xFFF5F5F5),
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedBorderColor = Color(0xFFFDB913)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Alamat Toko
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Alamat Toko",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black, fontFamily = plusjakarta,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = state.alamatToko,
                                    onValueChange = { viewModel.onEvent(DaftarTokoEvent.OnAlamatTokoChange(it)) },
                                    placeholder = {
                                        Text(
                                            text = "Masukkan Alamat Lengkap ....",
                                            color = Color.Gray
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Location",
                                            tint = Color.Gray
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedContainerColor = Color(0xFFF5F5F5),
                                        focusedContainerColor = Color(0xFFF5F5F5),
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedBorderColor = Color(0xFFFDB913)
                                    )
                                )
                            }

                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Simpan Profil Button
                    Button(
                        onClick = {
                            if (state.isEditMode) {
                                viewModel.onEvent(DaftarTokoEvent.OnUpdateToko)
                            } else {
                                viewModel.onEvent(DaftarTokoEvent.OnSaveToko)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 25.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFDB913)
                        ),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.Black
                            )
                        } else {
                            Text(
                                text = if (state.isEditMode) "Update Profil" else "Simpan Profil",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontFamily = plusjakarta
                            )
                        }
                    }
                }

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DaftarTokoPreview() {
    DaftarToko(navController = rememberNavController())
}