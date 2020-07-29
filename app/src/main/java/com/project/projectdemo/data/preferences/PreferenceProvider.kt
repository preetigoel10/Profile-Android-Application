package com.project.projectdemo.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val USER_LOGGED_IN = "user_logged_in"
private const val USER_USER_EMAIL = "user_user_email"
class PreferenceProvider(
    context: Context
) {
    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun saveUserLogInState(isUserLoggedIn: Boolean){
        preference.edit().putBoolean(
            USER_LOGGED_IN,
            isUserLoggedIn
        ).apply()
    }

    fun getUserLogInState(): Boolean{
        return preference.getBoolean(USER_LOGGED_IN,false)
    }

    fun saveUserEmail(userEmail: String){
        preference.edit().putString(
            USER_USER_EMAIL,
           userEmail
        ).apply()
    }
    fun getUserEmail(): String?{
        return preference.getString(USER_USER_EMAIL,null)
    }

}