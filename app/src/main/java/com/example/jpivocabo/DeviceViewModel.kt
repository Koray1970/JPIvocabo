package com.example.jpivocabo

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class DeviceViewModel @Inject constructor(private val deviceDBRepository: DeviceDBRepository) : ViewModel() {
class DeviceViewModel(private val deviceDBRepository: DeviceDBRepository) : ViewModel() {
    val allDevices:LiveData<List<Device>> = deviceDBRepository.allDevices.asLiveData()
    fun insert(device: Device) = viewModelScope.launch {
        deviceDBRepository.insert(device)
    }
    fun delete(device: Device) = viewModelScope.launch {
        deviceDBRepository.delete(device)
    }
}
class DeviceViewModelFactory(private val repository: DeviceDBRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeviceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeviceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}