package com.example.ngasiryuk.data.local.dao

import androidx.room.*
import com.example.ngasiryuk.data.local.entity.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customer ORDER BY createdAt DESC")
    fun getAllCustomer(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customer WHERE id = :id")
    suspend fun getCustomerById(id: Int): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    @Query("DELETE FROM customer")
    suspend fun deleteAllCustomer()
}

