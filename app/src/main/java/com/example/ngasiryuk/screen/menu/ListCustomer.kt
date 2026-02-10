package com.example.ngasiryuk.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.galonqu.commond.plusjakarta
import com.example.ngasiryuk.data.local.entity.CustomerEntity
import com.example.ngasiryuk.di.AppContainer
import com.example.ngasiryuk.screen.component.dialog.EditCustomerDialog
import com.example.ngasiryuk.screen.component.dialog.TambahCustomerDialog
import com.example.ngasiryuk.screen.menu.listcustomer.ListCustomerViewModel

@Composable
fun ListCustomer(
    navController: NavController,
    viewModel: ListCustomerViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AppContainer.provideListCustomerViewModel() as T
        }
    })
) {
    var showTambahCustomerDialog by remember { mutableStateOf(false) }
    var showEditCustomerDialog by remember { mutableStateOf(false) }
    var selectedCustomer by remember { mutableStateOf<CustomerEntity?>(null) }

    val customerList by viewModel.customerList.collectAsState()

    Scaffold(
        topBar = {
            // Custom Top Bar dengan Rounded Bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(Color(0xFFFDB913))
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Back Bulat
                    IconButton(
                        onClick = { navController.popBackStack()},
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = Color(0xFFD4A419),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Title
                    Text(
                        text = "Daftar Customer",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = plusjakarta
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTambahCustomerDialog = true },
                containerColor = Color(0xFFFDB913),
                contentColor = Color.Black,
                modifier = Modifier.padding(bottom = 35.dp).size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Customer",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        // Konten halaman
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(customerList.size) { index ->
                CustomerCard(
                    customer = customerList[index],
                    onEdit = {
                        selectedCustomer = customerList[index]
                        showEditCustomerDialog = true
                    },
                    onDelete = {
                        viewModel.deleteCustomer(customerList[index])
                    }
                )
            }
        }
    }

    // Dialog Tambah Customer
    if (showTambahCustomerDialog) {
        TambahCustomerDialog(
            onDismiss = { showTambahCustomerDialog = false },
            onSave = { nama, nomerWa, alamat ->
                viewModel.addCustomer(nama, nomerWa, alamat)
                showTambahCustomerDialog = false
            }
        )
    }

    // Dialog Edit Customer
    if (showEditCustomerDialog && selectedCustomer != null) {
        EditCustomerDialog(
            namaAwal = selectedCustomer!!.nama,
            nomorWaAwal = selectedCustomer!!.nomorWa,
            alamatAwal = selectedCustomer!!.alamat,
            onDismiss = {
                showEditCustomerDialog = false
                selectedCustomer = null
            },
            onSave = { nama, nomerWa, alamat ->
                viewModel.updateCustomer(selectedCustomer!!.id, nama, nomerWa, alamat)
                showEditCustomerDialog = false
                selectedCustomer = null
            }
        )
    }
}

@Composable
fun CustomerCard(
    customer: CustomerEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Customer Information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = customer.nama,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = customer.nomorWa,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = customer.alamat,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    fontFamily = plusjakarta
                )
            }

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Edit Button
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Delete Button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun ListCustomerPreview() {
    ListCustomer(navController = rememberNavController())

}