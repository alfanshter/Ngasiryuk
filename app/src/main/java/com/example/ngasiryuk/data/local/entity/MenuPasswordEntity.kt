package com.example.ngasiryuk.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_password")
data class MenuPasswordEntity(
    @PrimaryKey
    val menuName: String,
    val password: String,
    val isEnabled: Boolean = true
)

