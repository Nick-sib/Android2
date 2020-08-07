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
    private  val JSONDATA = "{\"coord\":{\"lon\":82.93,\"lat\":55.04},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"переменная облачность\",\"icon\":\"03n\"}],\"base\":\"stations\",\"main\":{\"temp\":18,\"feels_like\":19.62,\"temp_min\":18,\"temp_max\":18,\"pressure\":1013,\"humidity\":93},\"visibility\":10000,\"wind\":{\"speed\":1,\"deg\":320},\"clouds\":{\"all\":40},\"dt\":1596812115,\"sys\":{\"type\":1,\"id\":8958,\"country\":\"RU\",\"sunrise\":1596754084,\"sunset\":1596810002},\"timezone\":25200,\"id\":1496747,\"name\":\"Новосибирск\",\"cod\":200}"

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