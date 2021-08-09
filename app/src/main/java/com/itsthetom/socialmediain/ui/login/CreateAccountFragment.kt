package com.itsthetom.socialmediain.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.itsthetom.socialmediain.R
import com.itsthetom.socialmediain.databinding.FragmentCreateAccountBinding
import com.itsthetom.socialmediain.ui.profile.ProfileActivity


class CreateAccountFragment : Fragment() {



    private lateinit var binding:FragmentCreateAccountBinding
    private lateinit var viewModel: LoginViewModel
    private val signInLauncher=registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ){
        this.onSignInResult(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCreateAccountBinding.inflate(inflater)
        viewModel=ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        initUi()

        return binding.root
    }

    private fun initUi() {

        binding.alreadyHaveAnAccount.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.googleSignInButton.setOnClickListener {
            signInLauncher.launch(viewModel.initGoogleLogin())
        }

        binding.facebookSignInButton.setOnClickListener{
            signInLauncher.launch(viewModel.initFacebookLogin())
        }

        binding.btnSignUp.setOnClickListener{
            val fullName=binding.edName.text.toString().trim()
            val email=binding.edEmail.text.toString().trim()
            val password=binding.edPassword.text.toString().trim()
            if (email.isNullOrEmpty())
                binding.layoutEmailInput.error="Empty"
            else if (password.isNullOrEmpty())
                    binding.layoutPasswordInput.error="Empty"
            else if(fullName.isNullOrEmpty())
                    binding.layoutNameInput.error="Empty"
            else{
                initEmailSignUp(fullName,email,password)
            }
        }
    }

    private fun initEmailSignUp(fullName:String,email:String,password:String){
        val processingDialog= context?.let {
            MaterialAlertDialogBuilder(it).setView(R.layout.layout_loading)
                .setBackground(ResourcesCompat.getDrawable(resources,R.drawable.bg_transparent,null))
                .create()
        }
        processingDialog?.show()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    val user = FirebaseAuth.getInstance().currentUser
                    val updateProfile=UserProfileChangeRequest.Builder().setDisplayName(fullName).build()
                    user?.updateProfile(updateProfile)?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            processingDialog?.dismiss()
                            updateUI(user)
                        }
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Authentication failed due to : ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun onSignInResult(res: FirebaseAuthUIAuthenticationResult) {
        if(res.resultCode== Activity.RESULT_OK){
            val user = FirebaseAuth.getInstance().currentUser
            updateUI(user)
        }else{
            println("Signin failed")
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = FirebaseAuth.getInstance().currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser!=null){
            startActivity(Intent(requireActivity(), ProfileActivity::class.java))
            requireActivity().finishAffinity()
        }
    }

}