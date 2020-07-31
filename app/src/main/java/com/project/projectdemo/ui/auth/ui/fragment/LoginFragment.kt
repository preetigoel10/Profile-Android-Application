package com.project.projectdemo.ui.auth.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.project.projectdemo.R
import com.project.projectdemo.data.localdatabase.AppDatabase
import com.project.projectdemo.data.preferences.PreferenceProvider
import com.project.projectdemo.data.repositories.UserRepository
import com.project.projectdemo.databinding.LoginFragmentBinding
import com.project.projectdemo.ui.auth.ViewModelAuth
import com.project.projectdemo.ui.auth.ViewModelFactoryAuth
import com.project.projectdemo.ui.home.HomePageActivity
import com.project.projectdemo.util.toastFragment
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    lateinit var binding: LoginFragmentBinding
    lateinit var viewModel: ViewModelAuth
    lateinit var prefs: PreferenceProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.login_fragment,container,false)
        prefs = context?.let { PreferenceProvider(it) }!!
        val dao = AppDatabase.getInstance(context)!!.getUserDao()
        val repository = UserRepository(dao,prefs)
        val factoryAuth = ViewModelFactoryAuth(repository)
        viewModel = ViewModelProvider(this, factoryAuth).get(ViewModelAuth::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signUpText: TextView = view.findViewById(R.id.textSignUp)
        val buttonLogin: Button = view.findViewById(R.id.buttonLogin)
        val navController: NavController = Navigation.findNavController(view)

        signUpText.setOnClickListener{

            onClickSignUp(navController)
        }
        buttonLogin.setOnClickListener {
            onClickLogin()
        }

    }
    fun onClickSignUp(navController:NavController){
        navController.navigate(R.id.action_loginFragment_to_registerFragment)
    }
    fun onClickLogin()
    {
        viewModel.viewModelScope.launch {
            if (viewModel.authenticateUser()) {
                toastFragment(context,"Login success")
                prefs.saveUserEmail(viewModel.returnUsername())
                switchToHomepage()
            } else {
                toastFragment(context,"Invalid username or password")
            }
        }
    }

    fun switchToHomepage(){
        val intent = Intent(context, HomePageActivity::class.java)
        intent.putExtra("User email", prefs.getUserEmail() )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}


