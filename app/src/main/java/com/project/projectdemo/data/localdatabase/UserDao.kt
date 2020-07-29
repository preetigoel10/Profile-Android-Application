package com.project.projectdemo.data.localdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.projectdemo.data.entities.User


@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUser(user: User) : Long

    @Query("SELECT * FROM user WHERE email = :username")
    fun getUserLiveDataByEmail(username: String): LiveData<User>

    @Query("SELECT * FROM user WHERE email LIKE :userEmail")
    suspend fun getUser(userEmail: String): User

    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>
}

//    @Query("DELETE FROM user ")
//    suspend fun deleteAll()

//    @Update
//    suspend fun updateUser(user: User)

