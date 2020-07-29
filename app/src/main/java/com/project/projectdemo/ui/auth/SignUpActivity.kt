package com.project.projectdemo.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.project.projectdemo.R
import com.project.projectdemo.data.localdatabase.AppDatabase
import com.project.projectdemo.data.repositories.UserRepository
import com.project.projectdemo.databinding.SignupActivityBinding
import com.project.projectdemo.ui.home.HomePageActivity
import com.project.projectdemo.util.toast
import androidx.lifecycle.viewModelScope
import com.project.projectdemo.data.preferences.PreferenceProvider
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: SignupActivityBinding
    private lateinit var viewModel: ViewModelAuth
    private lateinit var prefs: PreferenceProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.signup_activity)
        prefs = PreferenceProvider(this)
        val dao = AppDatabase.getInstance(application)!!.getUserDao()
        val repository = UserRepository(dao,prefs)
        val factoryAuth = ViewModelFactoryAuth(repository)
        viewModel = ViewModelProvider(this, factoryAuth).get(ViewModelAuth::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
    fun onClickListener(view: View) {
        val rbg = findViewById<RadioGroup>(R.id.radioGroup1)
        val selected = rbg.checkedRadioButtonId
        val gender: RadioButton = findViewById<View>(selected) as RadioButton
        viewModel.radioChecked = gender.text.toString()
    }
    fun onClickSignUp(view: View){
        viewModel.viewModelScope.launch {
            if (viewModel.onClickRegister()&& viewModel.isFormValid) {
                prefs.saveUserEmail(viewModel.returnUsername())
                switchToHomepage()
           } else if(viewModel.isFormValid){
               toast("Account with this email already exists")
           }
       else{
           toast("Please fill all details!")
       }
    }
    }

    fun onClickSignIn(view: View){
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun switchToHomepage(){
        intent = Intent(this@SignUpActivity, HomePageActivity::class.java)
        intent.putExtra("User email", prefs.getUserEmail())
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}