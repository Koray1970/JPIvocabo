package com.example.jpivocabo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [(User::class), (Device::class)], version = 1, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun dbDao(): DBDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): AppRoomDatabase {
            // only one thread of execution at a time can enter this block of code
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "ivocabo_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(DevicaDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private class DevicaDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.dbDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(dbDao: DBDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            //wordDao.deleteAll()

            dbDao.getAllDevice()

            /*var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)*/
        }
    }
}