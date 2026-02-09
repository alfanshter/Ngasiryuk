package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.dao.CustomerDao
import com.example.ngasiryuk.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

class CustomerRepositoryImpl(private val customerDao: CustomerDao) : CustomerRepository {

    override fun getAllCustomer(): Flow<List<CustomerEntity>> {
        return customerDao.getAllCustomer()
    }

    override suspend fun getCustomerById(id: Int): CustomerEntity? {
        return customerDao.getCustomerById(id)
    }

    override suspend fun insertCustomer(nama: String, nomorWa: String, alamat: String): Long {
        val customer = CustomerEntity(nama = nama, nomorWa = nomorWa, alamat = alamat)
        return customerDao.insertCustomer(customer)
    }

    override suspend fun updateCustomer(id: Int, nama: String, nomorWa: String, alamat: String) {
        val existingCustomer = customerDao.getCustomerById(id)
        if (existingCustomer != null) {
            val updatedCustomer = existingCustomer.copy(
                nama = nama,
                nomorWa = nomorWa,
                alamat = alamat,
                updatedAt = System.currentTimeMillis()
            )
            customerDao.updateCustomer(updatedCustomer)
        }
    }

    override suspend fun deleteCustomer(customer: CustomerEntity) {
        customerDao.deleteCustomer(customer)
    }

    override suspend fun deleteAllCustomer() {
        customerDao.deleteAllCustomer()
    }
}

