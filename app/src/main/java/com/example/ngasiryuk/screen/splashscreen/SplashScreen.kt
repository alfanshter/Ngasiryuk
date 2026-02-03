package com.example.ngasiryuk.screen.splashscreen

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.ngasiryuk.R
import com.example.ngasiryuk.commond.orange

@Composable
fun SplashScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = orange), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.logoapk),
            contentScale = ContentScale.FillBounds,
            contentDescription = "logo"
        )

    }

}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}