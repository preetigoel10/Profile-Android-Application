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
    private lateinit var callbackFragment: CallbackFragment


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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
         val buttonLogin: Button = requireView().findViewById(R.id.buttonLogin)
         val signUpText: TextView = requireView().findViewById(R.id.textSignUp)
        buttonLogin.setOnClickListener {
            onClickLogin(it)
        }

        signUpText.setOnClickListener {
            onClickSignUp(it)
        }
    }


    fun onClickLogin(view: View)
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
    fun onClickSignUp(view: View){
        if(callbackFragment!= null){
            callbackFragment.changeFragmentLoginToSignUp()
        }
    }
    fun setCallbackFragment(callbackFragment: CallbackFragment){
        this.callbackFragment = callbackFragment
    }
    fun switchToHomepage(){
        val intent = Intent(context, HomePageActivity::class.java)
        intent.putExtra("User email", prefs.getUserEmail() )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}