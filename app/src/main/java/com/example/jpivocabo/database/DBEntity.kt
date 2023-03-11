package com.example.jpivocabo.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName="user")
data class User(
    @PrimaryKey val id:Int,
    @ColumnInfo(name="prvid") val prvid:String?,
    @ColumnInfo(name="registerdate") val registerdate:String?,
    @ColumnInfo(name="name") val name:String?,
    @ColumnInfo(name="lastname") val lastname:String?,
    @ColumnInfo(name="email") val email:String,
    @ColumnInfo(name="password") val password:String,
    @ColumnInfo(name="isauth") val isauth:Boolean,
) : Parcelable

@Parcelize
@Entity(tableName="devices")
data class Device(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id") val id:Int,
    @ColumnInfo(name="registered") val registerdate:String?,
    @ColumnInfo(name="mac-address") val macaddress:String?,
    @ColumnInfo(name="device-name") val name:String?,
    @ColumnInfo(name="latitude") val latitude:String?,
    @ColumnInfo(name="longitude") val longitude:String?
) : Parcelable

@Parcelize
@Entity(tableName="devicetracks")
data class DeviceLocationList(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id")  val id:Int,
    @ColumnInfo(name="registerdate") val registerdate:String?,
    @ColumnInfo(name="deviceid") val deviceid:Int,
    @ColumnInfo(name="latitude") val latitude:String,
    @ColumnInfo(name="longitude") val longitude:String
) : Parcelable

@Parcelize
@Entity(tableName="lostdevices")
data class LostDevices(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id")  val id:Int,
    @ColumnInfo(name="registerdate") val registerdate:String?,
    @ColumnInfo(name="deviceid") val deviceid:Int,
    @ColumnInfo(name="latitude") val latitude:String,
    @ColumnInfo(name="longitude") val longitude:String
) : Parcelable

@Parcelize
@Entity(tableName="lost-devices-archive")
data class LostDevicesArchive(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id")  val id:Int,
    @ColumnInfo(name="registerdate") val registerdate:String,
    @ColumnInfo(name="finddate") val finddate:Date,
    @ColumnInfo(name="deviceid") val deviceid:Int,
    @ColumnInfo(name="latitude") val latitude:String,
    @ColumnInfo(name="longitude") val longitude:String
) : Parcelable
