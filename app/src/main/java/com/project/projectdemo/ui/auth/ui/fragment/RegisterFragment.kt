package com.project.projectdemo.ui.auth.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.project.projectdemo.R
import com.project.projectdemo.data.localdatabase.AppDatabase
import com.project.projectdemo.data.preferences.PreferenceProvider
import com.project.projectdemo.data.repositories.UserRepository
import com.project.projectdemo.databinding.RegisterFragmentBinding
import com.project.projectdemo.ui.auth.ViewModelAuth
import com.project.projectdemo.ui.auth.ViewModelFactoryAuth
import com.project.projectdemo.ui.home.HomePageActivity
import com.project.projectdemo.util.toastFragment
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    lateinit var binding: RegisterFragmentBinding
    lateinit var prefs: PreferenceProvider
    private lateinit var viewModel: ViewModelAuth
    //private lateinit var callbackFragment: CallbackFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)
        prefs = context?.let { PreferenceProvider(it) }!!
        val dao = AppDatabase.getInstance(context)!!.getUserDao()
        val repository = UserRepository(dao, prefs)
        val factoryAuth = ViewModelFactoryAuth(repository)
        viewModel = ViewModelProvider(this, factoryAuth).get(ViewModelAuth::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonSignUp: Button = view.findViewById(R.id.signUpBtn)
        val rbMale: RadioButton = view.findViewById(R.id.radioButton1)
        val rbFemale: RadioButton = view.findViewById(R.id.radioButton2)
        val signInText: TextView = view.findViewById(R.id.tvSignIn)
        val navController: NavController = Navigation.findNavController(view)
        buttonSignUp.setOnClickListener {
            onClickSignUp()
        }
        signInText.setOnClickListener {
            onClickSignIn(navController)
        }
        rbMale.setOnClickListener {
            onClickListener()
        }
        rbFemale.setOnClickListener {
            onClickListener()
        }
    }

    fun onClickSignIn(navController: NavController) {
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.loginFragment,true).build()
        navController.navigate(R.id.action_registerFragment_to_loginFragment,null,navOptions)
    }


    fun onClickListener() {
        val rbg = getView()?.findViewById<RadioGroup>(R.id.radioGroup1)
        val selected = rbg?.checkedRadioButtonId
        val gender: RadioButton = selected?.let { getView()?.findViewById<View>(it) } as RadioButton
        viewModel.radioChecked = gender.text.toString()
    }

    fun onClickSignUp() {
        viewModel.viewModelScope.launch {
            if (viewModel.onClickRegister() && viewModel.isFormValid) {
                prefs.saveUserEmail(viewModel.returnUsername())
                switchToHomepage()
            } else if (viewModel.isFormValid) {
                toastFragment(context, "Account with this email already exists")
            } else {
                toastFragment(context, "Please fill all details!")
            }
        }
    }

    fun switchToHomepage() {
        val intent = Intent(context, HomePageActivity::class.java)
        intent.putExtra("User email", prefs.getUserEmail())
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}


//    fun onClickSignIn(view: View){
//       callbackFragment.changeFragmentSignUpToLogin()
//    }


//
//    fun setCallbackFragment(callbackFragment: CallbackFragment) {
//        this.callbackFragment = callbackFragment
//    }