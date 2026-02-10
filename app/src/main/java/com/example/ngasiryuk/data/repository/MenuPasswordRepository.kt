package com.example.ngasiryuk.data.repository

import com.example.ngasiryuk.data.local.dao.MenuPasswordDao
import com.example.ngasiryuk.data.local.entity.MenuPasswordEntity
import kotlinx.coroutines.flow.Flow

class MenuPasswordRepository(
    private val menuPasswordDao: MenuPasswordDao
) {

    fun getAllMenuPasswords(): Flow<List<MenuPasswordEntity>> {
        return menuPasswordDao.getAllMenuPasswords()
    }

    suspend fun getMenuPassword(menuName: String): MenuPasswordEntity? {
        return menuPasswordDao.getMenuPassword(menuName)
    }

    fun getMenuPasswordFlow(menuName: String): Flow<MenuPasswordEntity?> {
        return menuPasswordDao.getMenuPasswordFlow(menuName)
    }

    suspend fun saveMenuPassword(menuName: String, password: String, isEnabled: Boolean = true) {
        val menuPassword = MenuPasswordEntity(
            menuName = menuName,
            password = password,
            isEnabled = isEnabled
        )
        menuPasswordDao.insertMenuPassword(menuPassword)
    }

    suspend fun updateMenuPassword(menuPassword: MenuPasswordEntity) {
        menuPasswordDao.updateMenuPassword(menuPassword)
    }

    suspend fun deleteMenuPassword(menuName: String) {
        menuPasswordDao.deleteMenuPassword(menuName)
    }

    suspend fun isMenuPasswordEnabled(menuName: String): Boolean {
        return menuPasswordDao.isMenuPasswordEnabled(menuName)
    }

    suspend fun verifyPassword(menuName: String, inputPassword: String): Boolean {
        val storedPassword = menuPasswordDao.getPasswordForMenu(menuName)
        return storedPassword != null && storedPassword == inputPassword
    }

    suspend fun toggleMenuPassword(menuName: String, isEnabled: Boolean) {
        val menuPassword = menuPasswordDao.getMenuPassword(menuName)
        if (menuPassword != null) {
            menuPasswordDao.updateMenuPassword(menuPassword.copy(isEnabled = isEnabled))
        }
    }
}


