package com.example.jpivocabo

import android.bluetooth.BluetoothAdapter

class AppHelper {

        fun StringToMacaddress(str: String): String? {
            try {
                return str.replace("(..)(?!$)".toRegex(), "$1:")
                //return str.replace("..(?!\$)".toRegex(), "\$&:")
            } catch (ex: Exception) {
            }
            return null
        }

        fun CheckMacAddress(macaddress: String): Boolean {
            try {
                return BluetoothAdapter.checkBluetoothAddress(macaddress)
            } catch (ex: Exception) {

            }
            return false
        }

}