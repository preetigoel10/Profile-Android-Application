package com.project.projectdemo.data.repositories

import android.database.SQLException
import androidx.lifecycle.LiveData
import com.project.projectdemo.data.entities.User
import com.project.projectdemo.data.localdatabase.UserDao
import com.project.projectdemo.data.preferences.PreferenceProvider


class UserRepository(
    private val dao: UserDao,
    private val prefs: PreferenceProvider
) {
    val allUsers = dao.getAllUsers()

    suspend fun isValidAccount(username: String, password: String): Boolean {
            val userAccount: User = dao.getUser(username)
            if(userAccount != null) {
                prefs.saveUserLogInState(userAccount.password.equals(password))
                return userAccount.password.equals(password)
            }
        return false
    }
    fun getUserLiveDataByEmail(username: String): LiveData<User> {
        return dao.getUserLiveDataByEmail(username)
    }

    suspend fun insertUser(user: User): Boolean {
    try {
        dao.addUser(user)
    }
    catch(e: SQLException)
    {
        prefs.saveUserLogInState(false)
       return false
    }
    prefs.saveUserLogInState(true)
    return true
    }

}



//    suspend fun updateUser(user: User){
//        dao.updateUser(user)
//    }
//


//    suspend fun deleteAllUsers(){
//        dao.deleteAll()
//    }