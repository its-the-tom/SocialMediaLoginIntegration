package com.itsthetom.socialmediain.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.itsthetom.socialmediain.ui.profile.ProfileActivity
import com.itsthetom.socialmediain.databinding.ActivityMainBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(this, ProfileActivity::class.java))
            finishAffinity()
        }

    }



}