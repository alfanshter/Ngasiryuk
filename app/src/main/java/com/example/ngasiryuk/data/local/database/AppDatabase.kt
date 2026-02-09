package com.example.ngasiryuk.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ngasiryuk.data.local.dao.CustomerDao
import com.example.ngasiryuk.data.local.dao.KasirDao
import com.example.ngasiryuk.data.local.dao.KategoriDao
import com.example.ngasiryuk.data.local.dao.ProdukDao
import com.example.ngasiryuk.data.local.dao.RiwayatStokDao
import com.example.ngasiryuk.data.local.dao.TokoDao
import com.example.ngasiryuk.data.local.entity.CustomerEntity
import com.example.ngasiryuk.data.local.entity.KasirEntity
import com.example.ngasiryuk.data.local.entity.KategoriEntity
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import com.example.ngasiryuk.data.local.entity.TokoEntity

@Database(
    entities = [TokoEntity::class, KategoriEntity::class, ProdukEntity::class, RiwayatStokEntity::class, KasirEntity::class, CustomerEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tokoDao(): TokoDao
    abstract fun kategoriDao(): KategoriDao
    abstract fun produkDao(): ProdukDao
    abstract fun riwayatStokDao(): RiwayatStokDao
    abstract fun kasirDao(): KasirDao
    abstract fun customerDao(): CustomerDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ngasiryuk_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

