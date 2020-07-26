package com.nickolay.android2

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

const val PREFS_NAME = "com.nickolay.android2.MainActivity"
const val PREFS_KEY = "theme"
const val THEME_UNDEFINED = -1
const val THEME_LIGHT = 0
const val THEME_DARK = 1

class MainActivity:  AppCompatActivity() {

    private var isDark = false
    private val sharedPrefs by lazy {  getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
    private fun saveTheme(theme: Int) = sharedPrefs.edit().putInt(PREFS_KEY, theme).apply()
    private fun getSavedTheme() = sharedPrefs.getInt(PREFS_KEY, THEME_UNDEFINED)

    private var currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
    private lateinit var addVisibilityChanged: FloatingActionButton.OnVisibilityChangedListener

    fun changeFragment(index: Int){ //в последствии индекс заменить на фрагмент
        when (index) {
            1 -> {//changeCityList
                fab.hide(addVisibilityChanged)
                invalidateOptionsMenu()
                bottom_app_bar.navigationIcon = null
                screen_label.text = resources.getText(R.string.s_mi_city_list)
            }
            else -> {
                fab.hide(addVisibilityChanged)
                invalidateOptionsMenu()
                bottom_app_bar.navigationIcon = getDrawable(R.drawable.ic_menu_24)
                screen_label.text = resources.getText(R.string.s_fragment_primary)
            }
        }
    }

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }

    private fun initTheme() {
        val menuItem = bottom_app_bar.menu.getItem(0)
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
                .setAnchorView((if (fab.visibility == View.VISIBLE) fab else bottom_app_bar as View))
                .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initTheme()

        setSupportActionBar(bottom_app_bar)

        addVisibilityChanged = object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
                bottom_app_bar.toggleFabAlignment()
                bottom_app_bar.replaceMenu(
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
            if (bottom_app_bar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
                showSnackMessage(resources.getText(R.string.s_fab_city))
            else
                changeFragment(0)
            showSnackMessage(resources.getText(R.string.s_fab_apply))
        }
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
            R.id.mi_city_list -> showSnackMessage(resources.getText(R.string.s_mi_city_list))
            else -> showSnackMessage(item.title)
        }
        return true
    }


}