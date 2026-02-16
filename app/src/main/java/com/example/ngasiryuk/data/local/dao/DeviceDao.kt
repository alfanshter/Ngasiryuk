package com.example.ngasiryuk.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ngasiryuk.data.local.entity.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device LIMIT 1")
    suspend fun getDevice(): DeviceEntity?

    @Query("SELECT * FROM device LIMIT 1")
    fun getDeviceFlow(): Flow<DeviceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: DeviceEntity)

    @Update
    suspend fun updateDevice(device: DeviceEntity)

    @Query("UPDATE device SET lastAccess = :lastAccess WHERE deviceId = :deviceId")
    suspend fun updateLastAccess(deviceId: String, lastAccess: Long)

    @Query("DELETE FROM device")
    suspend fun deleteDevice()
}

