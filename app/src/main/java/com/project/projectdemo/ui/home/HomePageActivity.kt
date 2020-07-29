package com.project.projectdemo.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.projectdemo.R
import com.project.projectdemo.data.localdatabase.AppDatabase
import com.project.projectdemo.data.preferences.PreferenceProvider
import com.project.projectdemo.data.repositories.UserRepository
import com.project.projectdemo.databinding.ActivityHomePageBinding
import com.project.projectdemo.ui.auth.ViewModelAuth
import com.project.projectdemo.ui.auth.ViewModelFactoryAuth
import com.project.projectdemo.ui.auth.ui.fragment.LoginSignupActivity

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var viewModel: ViewModelAuth
    private lateinit var prefs: PreferenceProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home_page)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page)
        prefs = PreferenceProvider(this)
        val dao = AppDatabase.getInstance(application)!!.getUserDao()
        val repository = UserRepository(dao,prefs)
        val factoryAuth = ViewModelFactoryAuth(repository)
        viewModel = ViewModelProvider(this, factoryAuth).get(ViewModelAuth::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        displayUser()
    }

    //Getting details of user from ViewModel by observing the data returned
     private fun displayUser(){
         val intent = intent
         val userEmail = intent.getStringExtra("User email")
         viewModel.getUserData(userEmail)
         viewModel.user.observe(this, Observer {
             viewModel.userData(it)
         })
    }

    fun onClickSignOut(view: View){
        prefs.saveUserLogInState(false)
        intent = Intent(this,LoginSignupActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}