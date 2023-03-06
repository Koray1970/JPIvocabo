package com.example.jpivocabo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName="user")
data class User(
    @PrimaryKey val id:Int,
    @ColumnInfo(name="prvid") val prvid:String?,
    @ColumnInfo(name="registerdate") val registerdate:Date?,
    @ColumnInfo(name="name") val name:String?,
    @ColumnInfo(name="lastname") val lastname:String?,
    @ColumnInfo(name="email") val email:String,
    @ColumnInfo(name="password") val password:String,
    @ColumnInfo(name="isauth") val isauth:Boolean,
)


@Entity(tableName="devicelist")
data class Device(
    @PrimaryKey val id:Int,
    @ColumnInfo(name="registerdate") val registerdate:Date?,
    @ColumnInfo(name="macaddress") val macaddress:String?,
    @ColumnInfo(name="devicename") val name:String?,
    @ColumnInfo(name="latitude") val latitude:String?,
    @ColumnInfo(name="longitude") val longitude:String?
)
@Entity(tableName="devicetracks")
data class DeviceLocationList(
    @PrimaryKey val id:Int,
    @ColumnInfo(name="registerdate") val registerdate:Date?,
    @ColumnInfo(name="deviceid") val deviceid:Int,
    @ColumnInfo(name="latitude") val latitude:String,
    @ColumnInfo(name="longitude") val longitude:String
)
@Entity(tableName="lostdevices")
data class LostDevices(
    @PrimaryKey val id:Int,
    @ColumnInfo(name="registerdate") val registerdate:Date?,
    @ColumnInfo(name="deviceid") val deviceid:Int,
    @ColumnInfo(name="latitude") val latitude:String,
    @ColumnInfo(name="longitude") val longitude:String
)
@Entity(tableName="lostdevicesarchive")
data class LostDevicesArchive(
    @PrimaryKey val id:Int,
    @ColumnInfo(name="registerdate") val registerdate:Date,
    @ColumnInfo(name="finddate") val finddate:Date,
    @ColumnInfo(name="deviceid") val deviceid:Int,
    @ColumnInfo(name="latitude") val latitude:String,
    @ColumnInfo(name="longitude") val longitude:String
)
