package com.nickolay.android2.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import java.util.*

/*IntentService с API 21.0.0 помечен как deprecated
 *предлагают использовать вместо него JobIntentService*/
class GetCityWeather: JobIntentService() {
    private val TAG = "myLOG"

    companion object {
        private const val JOB_ID = 123

        var stop = false
        fun enqueueWork(cxt: Context, intent: Intent) {
            stop = false

            enqueueWork(cxt, GetCityWeather::class.java, JOB_ID, intent)
        }
    }


    override fun onCreate() {
        Log.d(TAG, "Сервис создан, скорее всего проверим интернет соеденение")
        super.onCreate()
    }

    override fun onHandleWork(intent: Intent) {
        val timeDelay = intent.getLongExtra("TimeDelay", 10 * 1000)
        var countRepeat = intent.getIntExtra("CountRepeat", 0)
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (stop || --countRepeat < 0) {
                    Log.d(TAG, "Закрываем интернет соединение ")
                    cancel()
                } else
                    Log.d(TAG, "Время проверить изменения погоды в выбранных городах: $countRepeat")
            }
        }, Date(), timeDelay)

    }


}