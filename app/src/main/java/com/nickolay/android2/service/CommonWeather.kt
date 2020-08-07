package com.nickolay.android2.service

import com.google.gson.Gson
import com.nickolay.android2.main.CityData
import com.nickolay.android2.model.WeatherRequest
import com.nickolay.android2.model.WeatherViewModel

object CommonWeather {
    private  val JSONDATA = "{\"coord\":{\"lon\":82.93,\"lat\":55.04},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"переменная облачность\",\"icon\":\"03n\"}],\"base\":\"stations\",\"main\":{\"temp\":18,\"feels_like\":19.62,\"temp_min\":18,\"temp_max\":18,\"pressure\":1013,\"humidity\":93},\"visibility\":10000,\"wind\":{\"speed\":1,\"deg\":320},\"clouds\":{\"all\":40},\"dt\":1596812115,\"sys\":{\"type\":1,\"id\":8958,\"country\":\"RU\",\"sunrise\":1596754084,\"sunset\":1596810002},\"timezone\":25200,\"id\":1496747,\"name\":\"Новосибирск\",\"cod\":200}"


    fun getData(viewModel: WeatherViewModel) {

        viewModel.setWeatherData(Gson().fromJson(
            JSONDATA,
            WeatherRequest::class.java
        ))
    }
}