package com.example.ngasiryuk.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ngasiryuk.data.local.dao.CustomerDao
import com.example.ngasiryuk.data.local.dao.DetailTransaksiDao
import com.example.ngasiryuk.data.local.dao.KasirDao
import com.example.ngasiryuk.data.local.dao.KategoriDao
import com.example.ngasiryuk.data.local.dao.ProdukDao
import com.example.ngasiryuk.data.local.dao.RiwayatStokDao
import com.example.ngasiryuk.data.local.dao.TokoDao
import com.example.ngasiryuk.data.local.dao.TransaksiDao
import com.example.ngasiryuk.data.local.entity.CustomerEntity
import com.example.ngasiryuk.data.local.entity.DetailTransaksiEntity
import com.example.ngasiryuk.data.local.entity.KasirEntity
import com.example.ngasiryuk.data.local.entity.KategoriEntity
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import com.example.ngasiryuk.data.local.entity.TokoEntity
import com.example.ngasiryuk.data.local.entity.TransaksiEntity

@Database(
    entities = [
        TokoEntity::class,
        KategoriEntity::class,
        ProdukEntity::class,
        RiwayatStokEntity::class,
        KasirEntity::class,
        CustomerEntity::class,
        TransaksiEntity::class,
        DetailTransaksiEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tokoDao(): TokoDao
    abstract fun kategoriDao(): KategoriDao
    abstract fun produkDao(): ProdukDao
    abstract fun riwayatStokDao(): RiwayatStokDao
    abstract fun kasirDao(): KasirDao
    abstract fun customerDao(): CustomerDao
    abstract fun transaksiDao(): TransaksiDao
    abstract fun detailTransaksiDao(): DetailTransaksiDao

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

