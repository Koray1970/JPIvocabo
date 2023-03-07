package com.example.jpivocabo

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DeviceDBRepository(private val dbDao: DBDao) {
    val allDevices: Flow<List<Device>> = dbDao.getAllDevice()

    //private val coroutineScope = CoroutineScope(Dispatchers.Main)
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(newDevice: Device) {
        dbDao.addDevice(newDevice)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(device: Device) {
        dbDao.deleteDevice(device)
    }

}