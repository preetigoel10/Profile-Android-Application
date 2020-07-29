package com.project.projectdemo.ui.auth

import android.util.Log
import androidx.lifecycle.*
import com.project.projectdemo.data.entities.User
import com.project.projectdemo.data.repositories.UserRepository
import java.text.SimpleDateFormat
import java.util.*


class ViewModelAuth(
    private val userRepository: UserRepository
):ViewModel() {
//    val users: LiveData<List<User>> = userRepository.allUsers

   lateinit var user: LiveData<User>

    var radioChecked : String? = null

    val userEmail = MutableLiveData<String>()

    val userPassword = MutableLiveData<String>()

    val userName = MutableLiveData<String>()

    val userGender = MutableLiveData<String>()

    val userCity = MutableLiveData<String>()

    val userAge = MutableLiveData<String>()

    var isFormValid:Boolean = true

   suspend fun onClickRegister(): Boolean {
       if(userName == null || userEmail == null || userPassword == null || userCity== null || radioChecked.isNullOrBlank() || userAge == null)
       {
           isFormValid = false
       }
       else{
           isFormValid = true
           val name = userName.value!!
           val email = userEmail.value!!
           val password = userPassword.value!!
           val city = userCity.value!!
           val gender = radioChecked
           val age = userAge.value!!.toInt()
           val date = Calendar.getInstance().time
           val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
           val formattedDate = formatter.format(date)
           return addUser(User(email, name, age, gender, city, password, formattedDate))
       }
       return isFormValid
   }
   suspend fun authenticateUser():Boolean{
        val email = userEmail.value!!
        val password = userPassword.value!!
        Log.i("TAG Auth","Started Authentication")
        val flag : Boolean
                flag = userRepository.isValidAccount(email, password)
       return flag
    }
    suspend fun addUser(user: User) : Boolean{
        return userRepository.insertUser(user)
    }
    fun returnUsername():String{
        return userEmail.value!!
    }

    fun getUserData(username:String){
        user = userRepository.getUserLiveDataByEmail(username)
    }

    fun userData(user: User){
        userName.value = user.name
        userEmail.value = user.email
        userCity.value = user.city
        userGender.value = user.gender
        userAge.value = user.age.toString()
    }

}