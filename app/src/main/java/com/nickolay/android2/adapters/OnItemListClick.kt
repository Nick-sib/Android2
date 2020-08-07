package com.nickolay.android2.adapters

import com.nickolay.android2.model.WeatherData


interface OnItemListClick {
    fun onSelectItem(weatherData: WeatherData)
}