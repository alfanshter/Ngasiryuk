package com.example.ngasiryuk.domain.model

data class DeviceInfo(
    val deviceId: String = "",
    val deviceName: String = "",
    val registeredAt: Long = System.currentTimeMillis(),
    val lastAccess: Long = System.currentTimeMillis()
)

