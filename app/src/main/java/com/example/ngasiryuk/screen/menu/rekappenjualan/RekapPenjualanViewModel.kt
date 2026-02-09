package com.example.ngasiryuk.screen.menu.rekappenjualan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ngasiryuk.data.local.entity.TransaksiEntity
import com.example.ngasiryuk.data.repository.TransaksiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class RekapPenjualanViewModel(
    private val transaksiRepository: TransaksiRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(Calendar.getInstance())
    val selectedDate: StateFlow<Calendar> = _selectedDate.asStateFlow()

    private val _transaksiList = MutableStateFlow<List<TransaksiEntity>>(emptyList())
    val transaksiList: StateFlow<List<TransaksiEntity>> = _transaksiList.asStateFlow()

    private val _totalPemasukan = MutableStateFlow(0)
    val totalPemasukan: StateFlow<Int> = _totalPemasukan.asStateFlow()

    private val _totalPengeluaran = MutableStateFlow(0)
    val totalPengeluaran: StateFlow<Int> = _totalPengeluaran.asStateFlow()

    private val _totalLabaBersih = MutableStateFlow(0)
    val totalLabaBersih: StateFlow<Int> = _totalLabaBersih.asStateFlow()

    private val _jumlahTransaksi = MutableStateFlow(0)
    val jumlahTransaksi: StateFlow<Int> = _jumlahTransaksi.asStateFlow()

    private val _pertumbuhanPersen = MutableStateFlow(0)
    val pertumbuhanPersen: StateFlow<Int> = _pertumbuhanPersen.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadRekapByDate()
    }

    fun setSelectedDate(calendar: Calendar) {
        _selectedDate.value = calendar
        loadRekapByDate()
    }

    private fun loadRekapByDate() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val calendar = _selectedDate.value
                val (startDate, endDate) = getDateRange(calendar)

                // Load transaksi berdasarkan tanggal
                launch {
                    transaksiRepository.getTransaksiByDateRange(startDate, endDate).collect { transaksiList ->
                        _transaksiList.value = transaksiList
                        _jumlahTransaksi.value = transaksiList.size
                    }
                }

                // Load statistik (menggunakan suspend function, bukan Flow)
                val pemasukan = transaksiRepository.getTotalPemasukanByDateRange(startDate, endDate)
                val pengeluaran = transaksiRepository.getTotalPengeluaranByDateRange(startDate, endDate)
                val labaBersih = transaksiRepository.getTotalLabaBersihByDateRange(startDate, endDate)

                _totalPemasukan.value = pemasukan
                _totalPengeluaran.value = pengeluaran
                _totalLabaBersih.value = labaBersih

                // Hitung pertumbuhan
                calculatePertumbuhan(calendar)

            } catch (e: Exception) {
                _errorMessage.value = "Error loading data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun calculatePertumbuhan(currentDate: Calendar) {
        try {
            // Ambil data kemarin
            val yesterdayCalendar = currentDate.clone() as Calendar
            yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1)
            val (yesterdayStart, yesterdayEnd) = getDateRange(yesterdayCalendar)

            val yesterdayLabaBersih = transaksiRepository.getTotalLabaBersihByDateRange(yesterdayStart, yesterdayEnd)

            if (yesterdayLabaBersih > 0) {
                val pertumbuhan = (((_totalLabaBersih.value - yesterdayLabaBersih).toDouble() / yesterdayLabaBersih) * 100).toInt()
                _pertumbuhanPersen.value = pertumbuhan
            } else {
                _pertumbuhanPersen.value = 0
            }
        } catch (e: Exception) {
            _pertumbuhanPersen.value = 0
        }
    }

    private fun getDateRange(calendar: Calendar): Pair<Long, Long> {
        // Awal hari (00:00:00)
        val startCalendar = calendar.clone() as Calendar
        startCalendar.set(Calendar.HOUR_OF_DAY, 0)
        startCalendar.set(Calendar.MINUTE, 0)
        startCalendar.set(Calendar.SECOND, 0)
        startCalendar.set(Calendar.MILLISECOND, 0)

        // Akhir hari (23:59:59)
        val endCalendar = calendar.clone() as Calendar
        endCalendar.set(Calendar.HOUR_OF_DAY, 23)
        endCalendar.set(Calendar.MINUTE, 59)
        endCalendar.set(Calendar.SECOND, 59)
        endCalendar.set(Calendar.MILLISECOND, 999)

        return Pair(startCalendar.timeInMillis, endCalendar.timeInMillis)
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

