package com.project.projectdemo.ui.auth


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.projectdemo.util.toast
import com.project.projectdemo.R
import com.project.projectdemo.data.localdatabase.AppDatabase
import com.project.projectdemo.data.preferences.PreferenceProvider
import com.project.projectdemo.data.repositories.UserRepository
import com.project.projectdemo.databinding.LoginActivityBinding
import com.project.projectdemo.ui.home.HomePageActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(){
    lateinit var binding: LoginActivityBinding
    lateinit var viewModel: ViewModelAuth
    lateinit var prefs: PreferenceProvider
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        prefs = PreferenceProvider(this)
        if(prefs.getUserLogInState()){
            switchToHomepage()
        }
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity)
        val dao = AppDatabase.getInstance(application)!!.getUserDao()
            val repository = UserRepository(dao,prefs)
            val factoryAuth = ViewModelFactoryAuth(repository)
            viewModel = ViewModelProvider(this, factoryAuth).get(ViewModelAuth::class.java)
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
    }

    fun onClickSignUp(view: View){
        val intent = Intent(this,SignUpActivity::class.java)
        startActivity(intent)
    }
    fun onClickLogin(view: View) {
        viewModel.viewModelScope.launch {
            if (viewModel.authenticateUser()) {
                toast("Login success")
               prefs.saveUserEmail(viewModel.returnUsername())
                switchToHomepage()
            } else {
                toast("Invalid username or password")
            }
        }

    }
    fun switchToHomepage(){
        val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
        intent.putExtra("User email", prefs.getUserEmail() )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}

//    private fun displayUser(){
//        viewModel.users.observe(this, Observer {
//            Log.i("TAG",it.toString())
//        })
//    }