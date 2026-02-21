package com.example.ngasiryuk.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.EscPosPrinter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ThermalPrinterHelper(context: Context) {

    private val bluetoothManager: BluetoothManager? =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager

    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

    /**
     * Mengecek apakah Bluetooth tersedia dan aktif
     */
    fun isBluetoothAvailable(): Boolean {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled
    }

    /**
     * Mendapatkan daftar printer Bluetooth yang sudah dipasangkan
     */
    @Suppress("MissingPermission")
    fun getPairedPrinters(): List<BluetoothDevice> {
        if (!isBluetoothAvailable()) {
            return emptyList()
        }
        return try {
            bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Mencetak struk transaksi
     */
    fun printReceipt(
        printerDevice: BluetoothDevice,
        namaUsaha: String,
        alamatUsaha: String,
        teleponUsaha: String,
        noTransaksi: String,
        tanggal: Date,
        namaKasir: String,
        namaCustomer: String?,
        items: List<ReceiptItem>,
        subtotal: Int,
        diskon: Int,
        total: Int,
        uangDibayar: Int,
        kembalian: Int,
        metodePembayaran: String,
        keterangan: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            @Suppress("MissingPermission")
            val connection = BluetoothConnection(printerDevice)
            val printer = EscPosPrinter(connection, 203, 48f, 32)

            val localeID = Locale("in", "ID")
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", localeID)

            // Build receipt text
            val receiptText = buildString {
                // Header
                append("[C]<font size='big'>$namaUsaha</font>\n")
                append("[C]$alamatUsaha\n")
                append("[C]Telp: $teleponUsaha\n")
                append("[C]================================\n")

                // Info Transaksi
                append("[L]No: $noTransaksi\n")
                append("[L]Tanggal: ${dateFormat.format(tanggal)}\n")
                append("[L]Kasir: $namaKasir\n")
                if (!namaCustomer.isNullOrBlank()) {
                    append("[L]Customer: $namaCustomer\n")
                }
                append("[L]--------------------------------\n")

                // Items
                items.forEach { item ->
                    append("[L]${item.nama}\n")
                    val hargaFormatted = formatRupiah(item.harga)
                    val totalFormatted = formatRupiah(item.harga * item.jumlah)
                    append("[L]  ${item.jumlah} x $hargaFormatted[R]$totalFormatted\n")
                }

                append("[L]--------------------------------\n")

                // Summary
                append("[L]Subtotal:[R]${formatRupiah(subtotal)}\n")
                if (diskon > 0) {
                    append("[L]Diskon:[R]${formatRupiah(diskon)}\n")
                }
                append("[L]<b>TOTAL:[R]${formatRupiah(total)}</b>\n")
                append("[L]--------------------------------\n")
                append("[L]Dibayar ($metodePembayaran):[R]${formatRupiah(uangDibayar)}\n")
                append("[L]Kembalian:[R]${formatRupiah(kembalian)}\n")

                if (!keterangan.isNullOrBlank()) {
                    append("[L]--------------------------------\n")
                    append("[L]Ket: $keterangan\n")
                }

                // Footer
                append("[C]--------------------------------\n")
                append("[C]Terima Kasih\n")
                append("[C]<font size='small'>Powered by Ngasiryuk</font>\n")
                append("[L]\n")
                append("[L]\n")
                append("[L]\n")
            }

            printer.printFormattedText(receiptText)
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e.message ?: "Gagal mencetak struk")
        }
    }

    /**
     * Test print untuk mengecek koneksi printer
     */
    fun testPrint(
        printerDevice: BluetoothDevice,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            @Suppress("MissingPermission")
            val connection = BluetoothConnection(printerDevice)
            val printer = EscPosPrinter(connection, 203, 48f, 32)

            val testText = buildString {
                append("[C]<font size='big'>TEST PRINT</font>\n")
                append("[C]================================\n")
                append("[L]Printer berhasil terhubung\n")
                val localeID = Locale("in", "ID")
                append("[L]Tanggal: ${SimpleDateFormat("dd/MM/yyyy HH:mm", localeID).format(Date())}\n")
                append("[C]================================\n")
                append("[C]<font size='small'>Ngasiryuk POS</font>\n")
                append("[L]\n")
                append("[L]\n")
            }

            printer.printFormattedText(testText)
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(e.message ?: "Gagal mencetak")
        }
    }

    private fun formatRupiah(amount: Int): String {
        return "Rp ${String.format(Locale("in", "ID"), "%,d", amount).replace(',', '.')}"
    }

    /**
     * Data class untuk item pada struk
     */
    data class ReceiptItem(
        val nama: String,
        val jumlah: Int,
        val harga: Int
    )
}





