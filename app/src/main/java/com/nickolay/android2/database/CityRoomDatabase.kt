package com.nickolay.android2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.Executors


@Database(entities = [CityTable::class], version = 1, exportSchema = false)
abstract class CityRoomDatabase: RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {

        @Volatile
        private var INSTANCE: CityRoomDatabase? = null
        private val NUMBER_OF_THREADS = 4

        val databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CityRoomDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityRoomDatabase::class.java,
                    "city_weather_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}