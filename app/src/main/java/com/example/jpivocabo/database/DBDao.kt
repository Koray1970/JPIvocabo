package com.example.jpivocabo.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DBDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User)

    @Query("SELECT * FROM user")
    fun findUser(): User

    @Insert
    fun insertDevice(device: Device)

    @Query("SELECT * FROM devices WHERE id= :id")
    fun findDeviceById(id:Int): Device

    @Query("SELECT * FROM devices")
    fun getAllDevice(): LiveData<List<Device>>

    @Delete
    suspend fun deleteDevice(device: Device)
}