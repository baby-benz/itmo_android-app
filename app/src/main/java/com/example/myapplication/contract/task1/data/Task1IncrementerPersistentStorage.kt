package com.example.myapplication.contract.task1.data

import android.content.SharedPreferences
import com.example.myapplication.contract.task1.Task1IncrementerContract

private const val SHARED_PREFERENCES_NUM_KEY = "Num"
private const val DEFAULT_NUM = 0

class IncrementerPersistentStorageModel(private val sharedPrefs: SharedPreferences) :
    Task1IncrementerContract.Model {
    override fun updateNum(value: Int) {
        sharedPrefs.edit().putInt(SHARED_PREFERENCES_NUM_KEY, value)?.apply()
    }

    override fun getNum(): Int {
        return sharedPrefs.getInt(SHARED_PREFERENCES_NUM_KEY, DEFAULT_NUM)
    }
}