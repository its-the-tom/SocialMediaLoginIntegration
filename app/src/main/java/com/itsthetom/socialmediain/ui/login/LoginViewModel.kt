package com.itsthetom.socialmediain.ui.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI

class LoginViewModel:ViewModel() {



     fun initGoogleLogin():Intent{

        val provides= arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
       return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(provides)
            .build()

    }

     fun initFacebookLogin(): Intent {
        val provides= arrayListOf(
            AuthUI.IdpConfig.FacebookBuilder().build()
        )
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(provides)
            .build()

    }

}