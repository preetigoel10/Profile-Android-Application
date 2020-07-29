package com.project.projectdemo.ui.auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.projectdemo.data.repositories.UserRepository
import java.lang.IllegalArgumentException

class ViewModelFactoryAuth(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        return super.create(modelClass)
        if(modelClass.isAssignableFrom(ViewModelAuth::class.java)) {
            return ViewModelAuth(userRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}