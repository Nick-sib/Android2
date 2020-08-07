package com.nickolay.android2

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.nickolay.android2.main.CityData
import com.nickolay.android2.main.SelectCity
import com.nickolay.android2.service.GetCityWeather
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


private const val PREFS_KEY = "theme"
const val THEME_UNDEFINED = -1
const val THEME_LIGHT = 0
const val THEME_DARK = 1

class MainActivity:  AppCompatActivity() {

    private lateinit var mSensorManager: SensorManager

    private var isDark = false
    private val sharedPrefs by lazy {  getSharedPreferences((MainActivity::class).qualifiedName, Context.MODE_PRIVATE) }

    private fun saveTheme(theme: Int) = sharedPrefs.edit().putInt(PREFS_KEY, theme).apply()
    private fun getSavedTheme() = sharedPrefs.getInt(PREFS_KEY, THEME_UNDEFINED)

    private var currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
    private lateinit var addVisibilityChanged: FloatingActionButton.OnVisibilityChangedListener

    fun changeFragment(index: Int){ //в последствии индекс заменить на фрагмент
        when (index) {
            1 -> {//changeCityList
                supportFragmentManager.beginTransaction().replace(R.id.fragmentPlace,
                    SelectCity.newInstance(0)).commit();

                fab.hide(addVisibilityChanged)
                invalidateOptionsMenu()
                bottomAppBar.navigationIcon = null
                //screenLabel.text = resources.getText(R.string.s_mi_city_list)
            }
            else -> {
                fab.hide(addVisibilityChanged)
                invalidateOptionsMenu()
                bottomAppBar.navigationIcon = getDrawable(R.drawable.ic_menu_24)
                screenLabel.text = resources.getText(R.string.s_fragment_primary)
            }
        }
    }

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }

    private fun initTheme() {
        val menuItem = bottomAppBar.menu.getItem(0)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P ||
                menuItem.itemId != R.id.mi_theme) {
            return
        }
        when (getSavedTheme()) {
            THEME_LIGHT -> {
                menuItem.setIcon(R.drawable.ic_night_24)
                isDark = false
            }
            THEME_DARK  -> {
                menuItem.setIcon(R.drawable.ic_day_24)
                isDark = true
            }
            else -> menuItem.setIcon(R.drawable.ic_night_24)
        }
    }

    fun showSnackMessage(text: CharSequence) {
        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT)
                .setAnchorView((if (fab.visibility == View.VISIBLE) fab else bottomAppBar as View))
                .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentPlace,
            CityData.newInstance(0)).commit();


        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
       /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            viewSensorValue.sensorTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
            viewSensorValue.sensorHimidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) // requires API level 14.
        }*/

        initTheme()

        setSupportActionBar(bottomAppBar)

        addVisibilityChanged = object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
                bottomAppBar.toggleFabAlignment()
                bottomAppBar.replaceMenu(
                        if (currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
                            R.menu.bottomappbar_menu_secondary
                        else R.menu.bottomappbar_menu_primary
                )
                fab?.setImageDrawable(
                        if (currentFabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
                            getDrawable(R.drawable.ic_check_24)
                        else
                            getDrawable(R.drawable.ic_change_city_24)
                )
                fab?.show()
            }
        }

//        toggle_fab_alignment_button.setOnClickListener {
//            fab.hide(addVisibilityChanged)
//            invalidateOptionsMenu()
//            bottom_app_bar.navigationIcon = if (bottom_app_bar.navigationIcon != null) null
//            else getDrawable(R.drawable.ic_menu_24)
//            when (screen_label.text) {
//                getString(R.string.primary_screen_text) -> screen_label.text =
//                    getString(R.string.secondary_sceen_text)
//                getString(R.string.secondary_sceen_text) -> screen_label.text =
//                    getString(R.string.primary_screen_text)
//            }
//        }

        fab.setOnClickListener {
            if (bottomAppBar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
                showSnackMessage(resources.getText(R.string.s_fab_city))
            else
                changeFragment(0)
            showSnackMessage(resources.getText(R.string.s_fab_apply))
        }
    }

    override fun onResume() {
        super.onResume()

        /*mSensorManager.registerListener(viewSensorValue, viewSensorValue.sensorTemperature, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(viewSensorValue, viewSensorValue.sensorHimidity,    SensorManager.SENSOR_DELAY_NORMAL)*/
    }

    override fun onPause() {
        super.onPause()
        //mSensorManager.unregisterListener(viewSensorValue)
    }

    private fun BottomAppBar.toggleFabAlignment() {
        currentFabAlignmentMode = fabAlignmentMode
        fabAlignmentMode = currentFabAlignmentMode.xor(1)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottomappbar_menu_primary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_theme -> {
                if (isDark)
                    setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
                else
                    setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
            }
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment(this)
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
            R.id.mi_city_list ->
                showSnackMessage(resources.getText(R.string.s_mi_city_list))
            R.id.mi_back -> {//не изменять список городов
                changeFragment(0)}
            else -> showSnackMessage(item.title)
        }

        return true
    }


    fun doJob(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent()
            intent.putExtra("CountRepeat", 10)
            intent.putExtra("TimeDelay", (10 * 1000 as Long))

            GetCityWeather.enqueueWork(this, intent)
        } else {/*пока просто заглушка нужен интерактор*/}
    }


}