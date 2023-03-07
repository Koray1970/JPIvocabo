package com.example.jpivocabo

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class IvocaboApplication: Application() {
    val applicationScope= CoroutineScope(SupervisorJob())
    val database by lazy { AppRoomDatabase.getDatabase(this,applicationScope)}
    val repository by lazy { DeviceDBRepository(database.dbDao()) }
}