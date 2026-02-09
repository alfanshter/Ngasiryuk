package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun getAllCustomer(): Flow<List<CustomerEntity>>
    suspend fun getCustomerById(id: Int): CustomerEntity?
    suspend fun insertCustomer(nama: String, nomorWa: String, alamat: String): Long
    suspend fun updateCustomer(id: Int, nama: String, nomorWa: String, alamat: String)
    suspend fun deleteCustomer(customer: CustomerEntity)
    suspend fun deleteAllCustomer()
}

