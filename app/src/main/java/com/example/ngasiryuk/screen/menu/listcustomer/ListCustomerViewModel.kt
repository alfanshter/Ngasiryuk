package com.example.ngasiryuk.screen.menu.listcustomer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ngasiryuk.data.local.entity.CustomerEntity
import com.example.ngasiryuk.data.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListCustomerViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _customerList = MutableStateFlow<List<CustomerEntity>>(emptyList())
    val customerList: StateFlow<List<CustomerEntity>> = _customerList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadCustomerList()
    }

    private fun loadCustomerList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                customerRepository.getAllCustomer().collect { customerList ->
                    _customerList.value = customerList
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading customer: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCustomer(nama: String, nomorWa: String, alamat: String) {
        viewModelScope.launch {
            try {
                customerRepository.insertCustomer(nama, nomorWa, alamat)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error adding customer: ${e.message}"
            }
        }
    }

    fun updateCustomer(id: Int, nama: String, nomorWa: String, alamat: String) {
        viewModelScope.launch {
            try {
                customerRepository.updateCustomer(id, nama, nomorWa, alamat)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error updating customer: ${e.message}"
            }
        }
    }

    fun deleteCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            try {
                customerRepository.deleteCustomer(customer)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting customer: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

