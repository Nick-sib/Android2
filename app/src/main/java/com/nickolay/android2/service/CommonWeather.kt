package com.nickolay.android2.service

import android.os.Handler
import android.util.Log
import com.google.gson.Gson
import com.nickolay.android2.BuildConfig
import com.nickolay.android2.model.WeatherRequest
import com.nickolay.android2.model.WeatherViewModel
import okhttp3.*
import java.io.IOException
import java.net.URL

object CommonWeather {

    private const val UNITS = "metric"
    private const val LANG = "ru"
    private const val WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?id=%s&units=$UNITS&lang=$LANG&appid=${BuildConfig.WEATHER_API_KEY}"


    fun getData(CITY_ID: Int, viewModel: WeatherViewModel) {
        val uri = URL(String.format(WEATHER_URL, CITY_ID))

        val client = OkHttpClient() // Клиент
        val builder = Request.Builder() // создадим строителя
        builder.url(uri) // укажем адрес сервера
        val request = builder.build() // построим запрос
        val call = client.newCall(request) // Ставим запрос в очередь
        call.enqueue(object : Callback {
            // этот хандлер нужен для синхронизации потоков, если его сконструировать
            // он запомнит поток и в дальнейшем будет с ним синхронизироваться
            val handler = Handler()

            // Это срабатывает по приходу ответа от сервера
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val answer = response.body()!!.string()
                // синхронизируем поток с потоком UI
                handler.post {
                    viewModel.setWeatherData(Gson().fromJson(
                        answer,
                        WeatherRequest::class.java
                    ))
                    //Log.d("myLOG", "onResponse: $answer")
                    //listener.onCompleted(answer) // вызовем наш метод обратного вызова
                }
            }

            // При сбое
            override fun onFailure(call: Call, e: IOException) {}
        })


    }
}