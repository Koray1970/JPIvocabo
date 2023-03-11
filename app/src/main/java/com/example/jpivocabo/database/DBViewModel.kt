package com.example.jpivocabo.database

import androidx.lifecycle.*
import com.example.jpivocabo.IvocaboApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DBViewModel(application: IvocaboApplication):AndroidViewModel(application) {
    val readAllDevice:LiveData<List<Device>>
    private val repository: DBRepository
    init {
        val dbDao= DBDatabase.getDatabase(application).dbDao()
        repository= DBRepository(dbDao)
        readAllDevice=repository.readAllDevice
    }
    fun addDevice(device: Device){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addDevice(device)
        }
    }

}
class DBViewModelFactory(
    private val application: IvocaboApplication
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(DBViewModel::class.java)) {
            return DBViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}