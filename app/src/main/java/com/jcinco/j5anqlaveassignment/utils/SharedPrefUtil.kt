package com.jcinco.j5anqlaveassignment.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtil {
    companion object {
        const val TAG:String = "SharedPrefUtil"
        @Volatile private var _instance: SharedPrefUtil? = null

        public fun getInstance(): SharedPrefUtil = _instance ?: synchronized(this) {
            _instance ?: SharedPrefUtil().also { _instance = it }
        }
    }

    private val SHARED_PREF_NAME = "com.jcinco.sharedpref"

    private var _context: Context? = null
    var context: Context?
    set(value) {
        _context = value
    }
    get() = _context


    private fun getPref(): SharedPreferences? {
        return _context?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    }


    fun save(key:String, value: String): Boolean {
        if (context != null) {
            val pref = getPref()
            with(pref?.edit()) {
                this?.putString(key, value)
                this?.commit()
            }
            return true
        }
        return false
    }

    /**
     * Returns the value associated to the specified key.
     *
     * @param String - the key
     * @return String - the value
     */
    fun get(key:String): String? {
        if (context != null) {
            val pref = getPref()
            return pref?.getString(key, null)
        }
        return null
    }

    /**
     * Checks if the specified key exists in the shared preferences file
     *
     * @return Boolean
     */
    fun hasKey(key:String):Boolean {
        return get(key) != null
    }
}