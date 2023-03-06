package com.example.jpivocabo

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(User::class)], version = 1, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun dbdao(): DBDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null
    }
}