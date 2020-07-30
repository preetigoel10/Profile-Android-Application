package com.project.projectdemo.ui.auth.ui.fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.project.projectdemo.R
import com.project.projectdemo.data.preferences.PreferenceProvider
import com.project.projectdemo.ui.home.HomePageActivity

class LoginSignupActivity : AppCompatActivity() {
    lateinit var fragment: Fragment
    lateinit var prefs: PreferenceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferenceProvider(this)
        if (prefs.getUserLogInState()) {
            switchToHomepage()
        }
        setContentView(R.layout.activity_login_signup)
//        addFragment()
    }

    private fun switchToHomepage() {
        val intent = Intent(this@LoginSignupActivity, HomePageActivity::class.java)
        intent.putExtra("User email", prefs.getUserEmail())
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}


//    override fun changeFragmentLoginToSignUp() {
//        replaceFragmentLoginToSignUp()
//    }

//    override fun changeFragmentSignUpToLogin() {
//        replaceFragmentSignUpToLogin()
//    }


//     private fun addFragment() {
//         fragment = LoginFragment()
//         (fragment as LoginFragment).setCallbackFragment(this)
//         supportFragmentManager.beginTransaction().apply {
//             add(R.id.fragmentContainer, fragment)
//             commit()
//         }
//     }

//    private fun replaceFragmentLoginToSignUp() {
//         fragment = RegisterFragment()
//        (fragment as RegisterFragment).setCallbackFragment(this)
//         supportFragmentManager.beginTransaction().apply {
//             addToBackStack(null)
//             replace(R.id.fragmentContainer, fragment)
//             commit()
//         }
//     }


//    private fun replaceFragmentSignUpToLogin() {
//         fragment = LoginFragment()
//        supportFragmentManager.popBackStack()
//     }