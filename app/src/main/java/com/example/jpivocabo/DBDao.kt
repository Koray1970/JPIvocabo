package com.example.jpivocabo

import androidx.room.*

@Dao
interface DBDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT TOP(1) * FROM user")
    fun findUser():User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDevice(device: Device)

    @Query("SELECT TOP(1) * FROM devicelist WHERE id= :id")
    fun findDevice(id:Int):Device

    @Query("SELECT * FROM devicelist")
    fun deviceList():List<Device>

    @Delete
    fun deleteDevice(device: Device)
}