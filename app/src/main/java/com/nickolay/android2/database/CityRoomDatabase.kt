package com.nickolay.android2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


@Database(entities = [CityTable::class], version = 1, exportSchema = false)
abstract class CityRoomDatabase: RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {

        @Volatile
        private var INSTANCE: CityRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CityRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityRoomDatabase::class.java,
                    "word_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(CityDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }


        private class CityDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.cityDao())
                    }
                }
            }

            fun populateDatabase(cityDao: CityDao) {
                cityDao.deleteAll()

                cityDao.insert(
                    CityTable(
                        1496747,
                        "Новосибирск",
                        0f,
                        0)
                )
            }
        }
    }
}