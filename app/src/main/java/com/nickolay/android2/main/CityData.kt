package com.nickolay.android2.main

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nickolay.android2.R
import com.nickolay.android2.model.WeatherData
import com.nickolay.android2.model.WeatherViewModel
import com.nickolay.android2.service.CommonWeather
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.data_city.*


class
CityData : Fragment()/*, OnItemListClick*/ {

    lateinit var viewModel: WeatherViewModel

    val sHumidity : String by lazy { resources.getString(R.string.t_humidity) }
    val sWind : String by lazy { resources.getString(R.string.t_wind) }
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(activity!!).get(WeatherViewModel::class.java)
        viewModel.weatherData.observe(
            this,
            androidx.lifecycle.Observer {
                compliteData(it)
            })
        if (savedInstanceState == null) {
            CommonWeather.getData(WeatherViewModel.DEFAULT_ID, viewModel)
        }


    }

    private fun compliteData(data: WeatherData) {
        //val icon = "https://openweathermap.org/img/wn/${data.icon}@4x.png"
        val uri = Uri.parse("https://openweathermap.org/img/wn/${data.icon}@4x.png")
        Picasso.get()!!
            .load(uri)
            .into(root.findViewById<ImageView>(R.id.ivCloudiness), object : Callback {
                override fun onSuccess() {
                    Log.d("myLOG", "success")
                }

                override fun onError(e: Exception?) {
                    Log.d("myLOG", "error")
                    Log.d("myLOG", e.toString())
                }
            })
        tvCityName.text = data.cityName
        tvDayOfWeek.text = data.dayWeek
        tvCloudiness.text = data.overcast
        tvTemperature.text = data.temp.toString()
        tvHumidity.text = String.format(sHumidity, data.humidity, "%")
        tvWind.text = String.format(sWind, data.wind)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val
        root = inflater.inflate(R.layout.data_city, container, false)

       // CommonWeather.getData(viewModel)

        //compliteData(viewModel.weatherData.value!!)
//        val uri = Uri.parse("https://openweathermap.org/img/wn/02d@4x.png")
//        Picasso.get()!!
//            .load(uri)
//            .into(root.findViewById<ImageView>(R.id.iv_Cloudiness))

//        for (i in delmeData.indices) {
//            delmeData[i] = 29 + (5 * random.nextDouble() - 3)
//        }
//        root.linechart.setChartData(delmeData, "Недельный прогноз")

//        val listner = View.OnClickListener{
//            onSelectItem(viewModel.adapter.getNext())
//        }

//        root.tv_CityName.setOnClickListener(listner)
//        root.setOnClickListener(listner)

        return root
    }

//    private fun compliteData(data: WeatherData) {
//        tv_CityName.text = data.cityName
//        tv_DayOfWeek.text = data.dayWeek
//        tv_Cloudiness.text = data.overcast
//        tv_Temperature.text = data.temp.toString()
//        tv_Humidity.text = String.format(sHumidity, data.humidity, "%")
//        tv_Wind.text = String.format(sWind, data.wind)
//
//        val resID = resources.getIdentifier(
//            data.icon,
//            "drawable",
//            context?.packageName
//        )
//        iv_Cloudiness.setImageResource(resID)
//    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): CityData {
            return CityData().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

//    override fun onSelectItem(weatherData: WeatherData) {
//        if (weatherData.isLoaded) {
//            compliteData(weatherData)
//        } else CommonWeather.getData(this, Handler(), weatherData.id)
//
//    }

}