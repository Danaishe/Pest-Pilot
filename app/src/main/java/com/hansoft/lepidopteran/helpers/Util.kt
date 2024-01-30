package com.hansoft.lepidopteran.helpers

import android.content.Context

class Util {
    fun saveLocalData(activity: Context, key: String?, value: String?) {
        val sharedPreferences =
            activity.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getLocalData(activity: Context, key: String?): String? {
        val sharedPreferences =
            activity.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }
}
