package com.example.ngasiryuk.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.ngasiryuk.screen.component.dialog.VerifikasiPasswordDialog
import com.example.ngasiryuk.viewmodel.MenuPasswordViewModel
import kotlinx.coroutines.launch

@Composable
fun PasswordProtectedScreen(
    menuName: String,
    viewModel: MenuPasswordViewModel,
    onAccessGranted: @Composable () -> Unit,
    onAccessDenied: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var isPasswordRequired by remember { mutableStateOf(false) }
    var isChecking by remember { mutableStateOf(true) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isAccessGranted by remember { mutableStateOf(false) }

    val verificationResult by viewModel.verificationResult.collectAsState()

    // Check if password is required
    LaunchedEffect(Unit) {
        scope.launch {
            isPasswordRequired = viewModel.isMenuPasswordEnabled(menuName)
            isChecking = false

            if (isPasswordRequired) {
                showPasswordDialog = true
            } else {
                isAccessGranted = true
            }
        }
    }

    // Handle verification result
    LaunchedEffect(verificationResult) {
        verificationResult?.let { isValid ->
            if (isValid) {
                isAccessGranted = true
                showPasswordDialog = false
                errorMessage = null
                viewModel.resetVerificationResult()
            } else {
                errorMessage = "Password salah! Silakan coba lagi."
                viewModel.resetVerificationResult()
            }
        }
    }

    if (!isChecking) {
        if (isAccessGranted) {
            onAccessGranted()
        } else if (showPasswordDialog) {
            VerifikasiPasswordDialog(
                menuName = menuName,
                onDismiss = {
                    showPasswordDialog = false
                    onAccessDenied()
                },
                onVerify = { inputPassword ->
                    viewModel.verifyPassword(menuName, inputPassword)
                },
                errorMessage = errorMessage
            )
        }
    }
}



