package com.itsthetom.socialmediain.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.firebase.auth.FirebaseAuth
import com.itsthetom.socialmediain.R
import com.itsthetom.socialmediain.databinding.ActivityProfileBinding
import com.itsthetom.socialmediain.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity() {
    private val firebaseAuth=FirebaseAuth.getInstance()
    private lateinit var binding:ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)




     firebaseAuth.currentUser?.let {
         binding.userName.text=it.displayName
         binding.userEmail.text=it.email

         Glide.with(this)
             .load(it.photoUrl)
             .placeholder(R.drawable.img_dummy_profile)
             .error(R.drawable.img_dummy_profile)
             .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
             .into(binding.profileImg)


         binding.btnSignOut.setOnClickListener{

             firebaseAuth.signOut()
             AuthUI.getInstance().signOut(this)
             Credentials.getClient(this).disableAutoSignIn()

             val intent= Intent(this, LoginActivity::class.java)
             startActivity(intent)
             finishAffinity()
         }
     }


    }
}