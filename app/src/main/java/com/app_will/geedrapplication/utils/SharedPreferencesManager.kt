package com.app_will.geedrapplication.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

data class User(
    val id: String
)


@Singleton
class SharedPreferencesManager @Inject constructor(
    private val myPref: SharedPreferences
) {
    private var user: User? = null

    fun createSession(id: String) {

        myPref.edit().apply {
            putString(MY_PREF_KEY_USERID, id)
            apply()
        }

        if (id != null) user = User(id)
    }

    fun loadSession() {
        val id = myPref.getString(MY_PREF_KEY_USERID, null)

        if (id != null) user = User(id)
    }

    fun logout() {
        myPref.edit().clear().apply()
        user = null
    }

    fun getUser(): User? {

        return user
    }
}