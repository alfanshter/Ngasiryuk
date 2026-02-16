package com.example.ngasiryuk.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.example.ngasiryuk.data.local.dao.DeviceDao
import com.example.ngasiryuk.data.local.dao.TokoDao
import com.example.ngasiryuk.data.local.entity.DeviceEntity
import com.example.ngasiryuk.domain.model.DeviceInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DeviceRepository(
    private val context: Context,
    private val deviceDao: DeviceDao,
    private val tokoDao: TokoDao
) {
    private val firestore = FirebaseFirestore.getInstance()
    private val maxDevices = 2 // Batas maksimal device yang diizinkan per toko

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID

        )
    }

    private fun getDeviceName(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }

    suspend fun checkDeviceAccess(): DeviceAccessResult {
        return try {
            val deviceId = getDeviceId()
            Log.d("DeviceRepository", "🔍 Checking device access for: $deviceId")

            // 1. Cek di Room Database lokal dulu
            val localDevice = deviceDao.getDevice()
            if (localDevice != null && localDevice.deviceId == deviceId) {
                Log.d("DeviceRepository", "✅ Device found in local Room DB")
                // Update last access di lokal
                deviceDao.updateLastAccess(deviceId, System.currentTimeMillis())
                return DeviceAccessResult.Allowed
            }

            Log.d("DeviceRepository", "📱 Device not in local DB, checking Firestore...")

            // 2. PERBAIKAN: Gunakan collection global "app_devices" untuk semua device
            // Tidak lagi per-user, tapi per-aplikasi
            val appId = context.packageName // com.example.ngasiryuk

            // Gunakan NonCancellable untuk Firestore operations
            withContext(NonCancellable) {
                val devicesRef = firestore.collection("app_devices")
                    .document(appId)
                    .collection("devices")

                Log.d("DeviceRepository", "🔥 Checking Firestore at: app_devices/$appId/devices/$deviceId")

                // Cek apakah device ini sudah terdaftar di Firestore
                val currentDevice = devicesRef.document(deviceId).get().await()

                if (currentDevice.exists()) {
                    Log.d("DeviceRepository", "✅ Device found in Firestore")
                    // Device sudah terdaftar di Firestore, simpan ke Room
                    val deviceInfo = currentDevice.toObject(DeviceInfo::class.java)
                    if (deviceInfo != null) {
                        val deviceEntity = DeviceEntity(
                            deviceId = deviceInfo.deviceId,
                            deviceName = deviceInfo.deviceName,
                            registeredAt = deviceInfo.registeredAt,
                            lastAccess = System.currentTimeMillis()
                        )
                        deviceDao.insertDevice(deviceEntity)
                        Log.d("DeviceRepository", "💾 Device saved to local Room DB")
                    }

                    // Update last access di Firestore
                    devicesRef.document(deviceId).update(
                        "lastAccess", System.currentTimeMillis()
                    ).await()

                    return@withContext DeviceAccessResult.Allowed
                }

                Log.d("DeviceRepository", "🆕 Device not registered yet. Checking device count...")

                // 3. Device belum terdaftar, cek jumlah device yang sudah terdaftar
                val registeredDevices = devicesRef.get().await()
                val deviceCount = registeredDevices.size()
                Log.d("DeviceRepository", "📊 Current device count: $deviceCount / $maxDevices")

                if (deviceCount >= maxDevices) {
                    Log.e("DeviceRepository", "❌ Device limit reached! ($deviceCount devices already registered)")

                    // Log semua device yang terdaftar untuk debugging
                    registeredDevices.documents.forEach { doc ->
                        val device = doc.toObject(DeviceInfo::class.java)
                        Log.d("DeviceRepository", "   📱 Registered: ${device?.deviceName} (${device?.deviceId})")
                    }

                    return@withContext DeviceAccessResult.LimitReached(deviceCount)
                }

                // 4. Masih di bawah limit, daftarkan device baru
                Log.d("DeviceRepository", "📝 Registering new device... (slot ${deviceCount + 1}/$maxDevices)")
                val newDevice = DeviceInfo(
                    deviceId = deviceId,
                    deviceName = getDeviceName()
                )

                // Simpan ke Firestore
                devicesRef.document(deviceId).set(newDevice).await()
                Log.d("DeviceRepository", "✅ Device registered in Firestore")

                // Simpan ke Room
                val deviceEntity = DeviceEntity(
                    deviceId = newDevice.deviceId,
                    deviceName = newDevice.deviceName,
                    registeredAt = newDevice.registeredAt,
                    lastAccess = newDevice.lastAccess
                )
                deviceDao.insertDevice(deviceEntity)
                Log.d("DeviceRepository", "✅ Device saved to local Room DB")

                DeviceAccessResult.Allowed
            }

        } catch (e: Exception) {
            Log.e("DeviceRepository", "❌ Error checking device access: ${e.message}", e)
            e.printStackTrace() // Print full stack trace untuk debugging
            DeviceAccessResult.Error(e.message ?: "Terjadi kesalahan yang tidak diketahui")
        }
    }

    suspend fun getRegisteredDevices(): List<DeviceInfo> {
        return try {
            val appId = context.packageName
            val snapshot = firestore.collection("app_devices")
                .document(appId)
                .collection("devices")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(DeviceInfo::class.java) }
        } catch (e: Exception) {
            Log.e("DeviceRepository", "Error getting devices: ${e.message}")
            emptyList()
        }
    }

    suspend fun clearLocalDevice() {
        deviceDao.deleteDevice()
    }

    // Test function untuk debug
    suspend fun testFirestoreConnection(): Boolean {
        return try {
            Log.d("DeviceRepository", "🧪 Testing Firestore connection...")

            val testData = hashMapOf(
                "test" to "connection_test",
                "timestamp" to System.currentTimeMillis(),
                "deviceId" to getDeviceId()
            )

            firestore.collection("test")
                .document("connection_test")
                .set(testData)
                .await()

            Log.d("DeviceRepository", "✅ Firestore connection test SUCCESS")
            true
        } catch (e: Exception) {
            Log.e("DeviceRepository", "❌ Firestore connection test FAILED: ${e.message}", e)
            false
        }
    }
}

sealed class DeviceAccessResult {
    object Allowed : DeviceAccessResult()
    object NoStoreYet : DeviceAccessResult()
    data class LimitReached(val deviceCount: Int) : DeviceAccessResult()
    data class Error(val message: String) : DeviceAccessResult()
}





