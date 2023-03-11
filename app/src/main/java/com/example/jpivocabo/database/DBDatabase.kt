package com.example.jpivocabo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[Device::class, User::class], version = 1)
abstract class DBDatabase:RoomDatabase() {
    abstract fun dbDao(): DBDao
    companion object {
        private var INSTANCE: DBDatabase?=null
        fun getDatabase(context:Context): DBDatabase {
            synchronized(this) {
                var instance= INSTANCE
                if(instance==null) {
                    instance= Room.databaseBuilder(
                        context,
                        DBDatabase::class.java, "ivocabodb"
                    ).build()
                    INSTANCE =instance
                }
                return instance
            }
        }
    }

}