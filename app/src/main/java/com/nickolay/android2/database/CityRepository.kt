package com.nickolay.android2.database


import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class CityRepository(private val cityDao: CityDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<ArrayList<CityTable>> = cityDao.getAlphabetizedWords()

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: CityTable) {
        cityDao.insert(word)
    }
}