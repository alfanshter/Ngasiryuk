package com.example.ngasiryuk.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ngasiryuk.data.local.entity.MenuPasswordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuPasswordDao {

    @Query("SELECT * FROM menu_password")
    fun getAllMenuPasswords(): Flow<List<MenuPasswordEntity>>

    @Query("SELECT * FROM menu_password WHERE menuName = :menuName")
    suspend fun getMenuPassword(menuName: String): MenuPasswordEntity?

    @Query("SELECT * FROM menu_password WHERE menuName = :menuName")
    fun getMenuPasswordFlow(menuName: String): Flow<MenuPasswordEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuPassword(menuPassword: MenuPasswordEntity)

    @Update
    suspend fun updateMenuPassword(menuPassword: MenuPasswordEntity)

    @Query("DELETE FROM menu_password WHERE menuName = :menuName")
    suspend fun deleteMenuPassword(menuName: String)

    @Query("SELECT EXISTS(SELECT 1 FROM menu_password WHERE menuName = :menuName AND isEnabled = 1)")
    suspend fun isMenuPasswordEnabled(menuName: String): Boolean

    @Query("SELECT password FROM menu_password WHERE menuName = :menuName AND isEnabled = 1")
    suspend fun getPasswordForMenu(menuName: String): String?
}

