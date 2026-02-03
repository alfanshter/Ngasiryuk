package com.example.ngasiryuk.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Topbar() {
    Scaffold(
        topBar = {
            // Custom Top Bar dengan Rounded Bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
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
                        color = Color.Black
                    )
                }
            }
        }
    ) { paddingValues ->
        // Konten halaman
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Tambahkan konten Anda di sini
            Text(
                text = "Konten Profil Toko",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopbarPreview() {
    Topbar()

}