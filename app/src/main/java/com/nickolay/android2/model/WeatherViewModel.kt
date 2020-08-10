package com.nickolay.android2.model


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.nickolay.android2.adapters.CityListAdapter
import com.nickolay.android2.database.CityRepository
import com.nickolay.android2.database.CityRoomDatabase
import com.nickolay.android2.database.CityTable
import java.text.SimpleDateFormat
import java.util.*


class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CityRepository
    val allCitys: LiveData<List<CityTable>>


    val adapter = CityListAdapter()
    val weatherData = MutableLiveData<WeatherData>()

    init {
        val cityDao = CityRoomDatabase.getDatabase(application, viewModelScope).cityDao()
        repository = CityRepository(cityDao)
        allCitys = repository.allWords
    }

    fun setWeatherData(weatherRequest: WeatherRequest) {
        val today = Calendar.getInstance().time
        weatherData.value = adapter.setData(weatherRequest, SimpleDateFormat("EEEE").format(today).capitalize())
    }

    companion object {
        const val DEFAULT_ID = 1496747
    }
}