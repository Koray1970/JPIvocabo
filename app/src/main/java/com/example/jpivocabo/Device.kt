package com.example.jpivocabo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName="userlist")
data class UserList(
    @PrimaryKey val id:Int,
    @ColumnInfo(name="registerdate") val registerdate:Date?,
    @ColumnInfo(name="name") val name:String?,
    @ColumnInfo(name="lastname") val lastname:String?,
    @ColumnInfo(name="email") val email:String,
    @ColumnInfo(name="password") val password:String,
    @ColumnInfo(name="lastlocation") val lastlocation:String?
)


@Entity(tableName="devicelist")
data class Device(
    @PrimaryKey val id:Int,
    @ColumnInfo(name="registerdate") val registerdate:Date?,
    @ColumnInfo(name="macaddress") val macaddress:String?,
    @ColumnInfo(name="devicename") val name:String?,
    @ColumnInfo(name="lastlocation") val lastlocation:String?
)
@Entity(tableName="devicelocationlist")
data class DeviceLocationList(
    @PrimaryKey val id:Int,
    @ColumnInfo(name="registerdate") val registerdate:Date?,
    @ColumnInfo(name="deviceid") val deviceid:Int,
    @ColumnInfo(name="lastlocation") val lastlocation:String?
)
