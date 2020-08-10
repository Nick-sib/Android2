package com.nickolay.android2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [CityTable::class], version = 1, exportSchema = false)
abstract class CityRoomDatabase: RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: CityRoomDatabase? = null

        fun getDatabase(context: Context): CityRoomDatabase {
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