package com.example.jpivocabo

import androidx.room.*

@Dao
interface DBDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user")
    fun findUser():User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDevice(device: Device)

    @Query("SELECT * FROM devices WHERE id= :id")
    fun findDevice(id:Int):Device

    @Query("SELECT * FROM devices")
    fun getAllDevice():List<Device>

    @Delete
    fun deleteDevice(device: Device)
}