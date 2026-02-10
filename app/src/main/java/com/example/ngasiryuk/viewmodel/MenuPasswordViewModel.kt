package com.example.ngasiryuk.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ngasiryuk.data.local.entity.MenuPasswordEntity
import com.example.ngasiryuk.data.repository.MenuPasswordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MenuPasswordViewModel(
    private val repository: MenuPasswordRepository
) : ViewModel() {

    companion object {
        private const val TAG = "MenuPasswordViewModel"
    }

    private val _menuPasswords = MutableStateFlow<Map<String, MenuPasswordEntity>>(emptyMap())
    val menuPasswords: StateFlow<Map<String, MenuPasswordEntity>> = _menuPasswords.asStateFlow()

    private val _verificationResult = MutableStateFlow<Boolean?>(null)
    val verificationResult: StateFlow<Boolean?> = _verificationResult.asStateFlow()

    init {
        Log.d(TAG, "ViewModel initialized")
        loadAllMenuPasswords()
    }

    private fun loadAllMenuPasswords() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Loading menu passwords...")
                repository.getAllMenuPasswords()
                    .catch { e ->
                        Log.e(TAG, "Error loading menu passwords", e)
                    }
                    .collect { list ->
                        Log.d(TAG, "Loaded ${list.size} menu passwords")
                        _menuPasswords.value = list.associateBy { it.menuName }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in loadAllMenuPasswords", e)
            }
        }
    }

    fun saveMenuPassword(menuName: String, password: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Saving password for menu: $menuName")
                repository.saveMenuPassword(menuName, password, true)
                Log.d(TAG, "Password saved successfully for: $menuName")
            } catch (e: Exception) {
                Log.e(TAG, "Error saving password for $menuName", e)
            }
        }
    }

    fun deleteMenuPassword(menuName: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Deleting password for menu: $menuName")
                repository.deleteMenuPassword(menuName)
                Log.d(TAG, "Password deleted successfully for: $menuName")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting password for $menuName", e)
            }
        }
    }

    suspend fun isMenuPasswordEnabled(menuName: String): Boolean {
        return try {
            val enabled = repository.isMenuPasswordEnabled(menuName)
            Log.d(TAG, "Menu $menuName password enabled: $enabled")
            enabled
        } catch (e: Exception) {
            Log.e(TAG, "Error checking if menu password enabled for $menuName", e)
            false
        }
    }

    fun verifyPassword(menuName: String, inputPassword: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Verifying password for menu: $menuName")
                val isValid = repository.verifyPassword(menuName, inputPassword)
                Log.d(TAG, "Password verification result for $menuName: $isValid")
                _verificationResult.value = isValid
            } catch (e: Exception) {
                Log.e(TAG, "Error verifying password for $menuName", e)
                _verificationResult.value = false
            }
        }
    }

    fun resetVerificationResult() {
        Log.d(TAG, "Resetting verification result")
        _verificationResult.value = null
    }

    fun toggleMenuPassword(menuName: String, isEnabled: Boolean) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Toggling password for $menuName to: $isEnabled")
                if (isEnabled) {
                    // Jika enable, tidak langsung toggle karena harus set password dulu
                    // Ini akan dihandle di UI
                    Log.d(TAG, "Enable requested - will be handled by UI")
                } else {
                    // Jika disable, hapus password
                    repository.deleteMenuPassword(menuName)
                    Log.d(TAG, "Password toggled off for: $menuName")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error toggling password for $menuName", e)
            }
        }
    }
}




