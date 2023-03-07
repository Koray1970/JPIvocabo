package com.example.jpivocabo

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceDBRepository(private val dbDao: DBDao) {
    val allDevices = MutableLiveData<List<Device>>()
    val foundDevice = MutableLiveData<Device>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun addEmployee(newDevice: Device) {
        coroutineScope.launch(Dispatchers.IO) {
            dbDao.addDevice(newDevice)
        }
    }
}