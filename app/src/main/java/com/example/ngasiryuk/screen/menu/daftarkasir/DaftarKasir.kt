package com.example.ngasiryuk.screen.menu.daftarkasir

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.screen.component.dialog.TambahKasirDialog

@Composable
fun DaftarKasir(navController: NavController) {
    var showTambahKasirDialog by remember { mutableStateOf(false) }
    var kasirList by remember {
        mutableStateOf(
            mutableListOf(
                "Muhib Goat",
                "Autan liquid",
                "Autan liquid",
                "Autan liquid"
            )
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
                        text = "Daftar Kasir",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = plusjakarta
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTambahKasirDialog = true },
                containerColor = Color(0xFFFDB913),
                contentColor = Color.Black,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Kasir",
                    modifier = Modifier.size(32.dp)
                )
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(kasirList.size) { index ->
                KasirCard(
                    namaKasir = kasirList[index],
                    onEdit = { /* Handle edit */ },
                    onDelete = {
                        kasirList = kasirList.toMutableList().apply {
                            removeAt(index)
                        }
                    }
                )
            }
        }
    }

    // Dialog Tambah Kasir
    if (showTambahKasirDialog) {
        TambahKasirDialog(
            onDismiss = { showTambahKasirDialog = false },
            onSave = { namaKasir ->
                kasirList = kasirList.toMutableList().apply {
                    add(namaKasir)
                }
                showTambahKasirDialog = false
            }
        )
    }
}

@Composable
fun KasirCard(
    namaKasir: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
            // Nama Kasir
            Text(
                text = namaKasir,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                fontFamily = plusjakarta,
                modifier = Modifier.weight(1f)
            )

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Edit Button
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Delete Button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
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
    }
}



@Preview(showBackground = true)
@Composable
private fun DaftarKasirPreview() {
    DaftarKasir(navController = rememberNavController())

}