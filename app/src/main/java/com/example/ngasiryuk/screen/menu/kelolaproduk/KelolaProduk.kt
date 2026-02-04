package com.example.ngasiryuk.screen.menu.kelolaproduk

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.R

@Composable
fun KelolaProduk() {
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
                    modifier = Modifier.fillMaxSize().padding(top = 30.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
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
                            text = "Kelola Produk",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = plusjakarta
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = { /* Handle search */ },
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = Color(0xFFD4A419),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    // Tab Row (Stok & Riwayat)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        var selectedTab by remember { mutableStateOf(0) }

                        // Tab Stok
                        Button(
                            onClick = { selectedTab = 0 },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTab == 0) Color.White else Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            elevation = if (selectedTab == 0) ButtonDefaults.buttonElevation(4.dp) else null
                        ) {
                            Text(
                                text = "Stok",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = plusjakarta
                            )
                        }

                        // Tab Riwayat
                        Button(
                            onClick = { selectedTab = 1 },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTab == 1) Color.White else Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            elevation = if (selectedTab == 1) ButtonDefaults.buttonElevation(4.dp) else null
                        ) {
                            Text(
                                text = "Riwayat",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = plusjakarta
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle add product */ },
                containerColor = Color(0xFFFDB913),
                contentColor = Color.Black,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product",
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
            items(3) { index ->
                ProductCard(
                    isHighlighted = index == 0
                )
            }
        }
    }
}

@Composable
fun ProductCard(isHighlighted: Boolean = false) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Autan liquid",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = plusjakarta
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Ready Badge
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF4CAF50),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Ready",
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "SKU : 8899222019I0101",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "STOK : 8",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Kategori : Body Lotion",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = plusjakarta
                    )
                }

                // Action Buttons
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { /* Handle edit */ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = { /* Handle delete */ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = { /* Handle QR */ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.qrcode),
                            contentDescription = "QR Code",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Harga Beli : Rp 1.000",
                    fontSize = 14.sp,
                    color = Color(0xFFFDB913),
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusjakarta
                )
                Text(
                    text = "Harga Jual : Rp 1.500",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                    fontFamily = plusjakarta
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KelolaProdukPreview() {
    KelolaProduk()

}