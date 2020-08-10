package com.nickolay.android2.database


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {

    @Query("SELECT * from city_table ORDER BY city_name ASC")
    fun getAlphabetizedWords(): LiveData<List<CityTable>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cityTable: CityTable)

    @Query("DELETE FROM city_table")
    suspend fun deleteAll()

    @Update
    fun updateStudent(cityTable: CityTable)

}