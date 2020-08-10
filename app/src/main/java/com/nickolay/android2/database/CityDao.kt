package com.nickolay.android2.database


import androidx.room.*

@Dao
interface CityDao {

    @Query("SELECT * from city_table ORDER BY _id ASC")
    fun getAlphabetizedWords(): List<CityTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cityTable: CityTable)

    @Query("DELETE FROM city_table")
    suspend fun deleteAll()

    @Update
    fun updateStudent(cityTable: CityTable)

}