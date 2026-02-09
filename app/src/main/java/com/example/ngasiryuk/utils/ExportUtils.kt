package com.example.ngasiryuk.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.ngasiryuk.data.local.entity.ProdukEntity
import com.example.ngasiryuk.data.local.entity.RiwayatStokEntity
import java.io.File
import java.io.FileWriter
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object ExportUtils {

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    private val fileNameFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    /**
     * Export Stok to CSV (can be opened in Excel)
     */
    fun exportStokToExcel(
        context: Context,
        produkList: List<ProdukEntity>
    ): File {
        val fileName = "Laporan_Stok_${fileNameFormat.format(Date())}.csv"
        val file = File(context.getExternalFilesDir(null), fileName)

        FileWriter(file).use { writer ->
            // Write header
            writer.append("No,Nama Produk,SKU,Kategori,Stok,Harga Beli,Harga Jual,Status\n")

            // Write data
            produkList.forEachIndexed { index, produk ->
                writer.append("${index + 1},")
                writer.append("\"${produk.namaProduk}\",")
                writer.append("\"${produk.sku}\",")
                writer.append("\"${produk.kategoriNama}\",")
                writer.append("${produk.stok},")
                writer.append("${produk.hargaBeli},")
                writer.append("${produk.hargaJual},")
                writer.append("\"${produk.status}\"\n")
            }
        }

        return file
    }

    /**
     * Export Barang Masuk to CSV
     */
    fun exportBarangMasukToExcel(
        context: Context,
        riwayatList: List<RiwayatStokEntity>
    ): File {
        val fileName = "Laporan_Barang_Masuk_${fileNameFormat.format(Date())}.csv"
        val file = File(context.getExternalFilesDir(null), fileName)

        FileWriter(file).use { writer ->
            // Write header
            writer.append("No,Nama Produk,Jumlah Masuk,Keterangan,Stok Sebelum,Stok Sesudah,Tanggal\n")

            // Write data
            riwayatList.forEachIndexed { index, riwayat ->
                writer.append("${index + 1},")
                writer.append("\"${riwayat.namaProduk}\",")
                writer.append("${riwayat.jumlah},")
                writer.append("\"${riwayat.keterangan}\",")
                writer.append("${riwayat.stokSebelum},")
                writer.append("${riwayat.stokSesudah},")
                writer.append("\"${dateFormat.format(Date(riwayat.createdAt))}\"\n")
            }
        }

        return file
    }

    /**
     * Export Barang Keluar to CSV
     */
    fun exportBarangKeluarToExcel(
        context: Context,
        riwayatList: List<RiwayatStokEntity>
    ): File {
        val fileName = "Laporan_Barang_Keluar_${fileNameFormat.format(Date())}.csv"
        val file = File(context.getExternalFilesDir(null), fileName)

        FileWriter(file).use { writer ->
            // Write header
            writer.append("No,Nama Produk,Jumlah Keluar,Keterangan,Stok Sebelum,Stok Sesudah,Tanggal\n")

            // Write data
            riwayatList.forEachIndexed { index, riwayat ->
                writer.append("${index + 1},")
                writer.append("\"${riwayat.namaProduk}\",")
                writer.append("${kotlin.math.abs(riwayat.jumlah)},")
                writer.append("\"${riwayat.keterangan}\",")
                writer.append("${riwayat.stokSebelum},")
                writer.append("${riwayat.stokSesudah},")
                writer.append("\"${dateFormat.format(Date(riwayat.createdAt))}\"\n")
            }
        }

        return file
    }

    /**
     * Share file (for opening with external apps)
     */
    fun shareFile(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "text/csv")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(Intent.createChooser(intent, "Buka dengan"))
        } catch (e: Exception) {
            // If no app can handle CSV, try sharing instead
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Bagikan file"))
        }
    }
}


