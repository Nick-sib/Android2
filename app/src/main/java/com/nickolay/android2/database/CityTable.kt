package com.nickolay.android2.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class CityTable(
        @PrimaryKey @ColumnInfo(name = "_id") val id: Int,
        val city_name: String,
        var city_tempricha: Float,
        var city_data: Int
)