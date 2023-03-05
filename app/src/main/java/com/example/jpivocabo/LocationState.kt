package com.example.jpivocabo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/*data class LocationState(
    var currentlocation: LatLng?
)*/
@HiltViewModel
class LocationStateViewModel @Inject constructor() : ViewModel() {
    var locationStateData by mutableStateOf(LatLng(1.35, 103.87))
        private set

    /* var locationStateData by mutableStateOf(LocationState(currentlocation = LatLng(1.35, 103.87)))
     private set*/
    fun onChange(newLatLng: LatLng?) {
        if (newLatLng != null)
            locationStateData = newLatLng
    }

    /*fun getValue() {
        viewModelScope.launch {
        return locationStateData
        }
        return null
    }*/
}
