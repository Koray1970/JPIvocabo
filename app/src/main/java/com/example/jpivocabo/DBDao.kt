package com.example.jpivocabo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DBDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user")
    fun findUser():User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDevice(device: Device)

    @Query("SELECT * FROM devices WHERE id= :id")
    fun findDeviceById(id:Int):Device

    @Query("SELECT * FROM devices")
    fun getAllDevice(): Flow<List<Device>>

    @Delete
    suspend fun deleteDevice(device: Device)
}