package com.example.jpivocabo.database

import androidx.lifecycle.LiveData

class DBRepository(private val dbDao: DBDao) {
    val readAllDevice: LiveData<List<Device>> = dbDao.getAllDevice()
    suspend fun addDevice(device: Device) {
        dbDao.insertDevice(device)
    }

    suspend fun deleteDevice(device: Device) {
        dbDao.deleteDevice(device)
    }
}