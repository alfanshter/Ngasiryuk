package com.example.ngasiryuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device")
data class DeviceEntity(
    @PrimaryKey
    val deviceId: String,
    val deviceName: String,
    val registeredAt: Long = System.currentTimeMillis(),
    val lastAccess: Long = System.currentTimeMillis()
)

