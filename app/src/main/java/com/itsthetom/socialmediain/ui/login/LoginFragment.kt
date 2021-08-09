package com.itsthetom.socialmediain.ui.login

import android.app.Activity.RESULT_OK
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
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseUser
import com.itsthetom.socialmediain.ui.profile.ProfileActivity
import com.itsthetom.socialmediain.R
import com.itsthetom.socialmediain.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth as FirebaseAuth


class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    val TAG="TAG"
    private val EMAIL="email"
    private val auth=FirebaseAuth.getInstance()
    private val signInLauncher=registerForActivityResult(
        FirebaseAuthUIActivityResultContract()){
        this.onSignInResult(it)
    }

    private lateinit var viewModel:LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(inflater)
        viewModel=ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        initUi();


        return binding.root
    }

      
    private fun initUi() {
        binding.btnSignup.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }

        binding.btnForgetPassword.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }


        binding.edEmail.setOnClickListener {
            binding.layoutEmailInput.error=""
        }
        binding.edPassword.setOnClickListener{
            binding.layoutPasswordInput.error=""
        }

        binding.btnLogin.setOnClickListener{
            validateEmailAndPassword()
        }

        binding.googleSignInButton.setOnClickListener{
            signInLauncher.launch(viewModel.initGoogleLogin())
        }

       binding.facebookSignInButton.setOnClickListener{
           signInLauncher.launch(viewModel.initFacebookLogin())
       }
    }

    private fun validateEmailAndPassword() {
            if (binding.edEmail.text.toString().trim().isEmpty()){
                binding.layoutEmailInput.error="Empty"
            }else if(binding.edPassword.text.toString().trim().isEmpty()){
                     binding.layoutPasswordInput.error="Empty"
            }else{
                binding.layoutEmailInput.refreshErrorIconDrawableState()
                initEmailLogin(binding.edEmail.text.toString().trim(),binding.edPassword.text.toString().trim())
            }
    }

    private fun initEmailLogin(email:String,password:String){
        val processingDialog= context?.let {
            MaterialAlertDialogBuilder(it).setView(R.layout.layout_loading)
                .setBackground(ResourcesCompat.getDrawable(resources,R.drawable.bg_transparent,null))
                .create()
        }
        processingDialog?.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    processingDialog?.dismiss()
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    processingDialog?.dismiss()
                    Toast.makeText(context,"Login Error: "+task.exception?.message,Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun onSignInResult(res: FirebaseAuthUIAuthenticationResult) {
        if(res.resultCode==RESULT_OK){
            val user = FirebaseAuth.getInstance().currentUser

            updateUI(user)
        }else{
            println("Signin failed")
        }
    }

    override fun onStart() {
        super.onStart()
            // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser!=null){

            startActivity(Intent(requireActivity(), ProfileActivity::class.java))
            requireActivity().finishAffinity()
        }
    }

}

