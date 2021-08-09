package com.itsthetom.socialmediain.ui.login

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.itsthetom.socialmediain.R
import com.itsthetom.socialmediain.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {

    private lateinit var binding:FragmentForgetPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentForgetPasswordBinding.inflate(inflater)

        initUI()

        return binding.root
    }

    private fun initUI() {
        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.btnGoToLogin.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.edEmail.setOnClickListener{
            binding.layoutEmailInput.error=""
        }


        binding.btnSendResetLink.setOnClickListener{
            if (binding.edEmail.text.toString().trim().isNullOrEmpty()){
                binding.layoutEmailInput.error="Empty"
            }else{
                sendResetLink(binding.edEmail.text.toString().trim())
            }
        }

    }

    private fun sendResetLink(emailAddress:String){
        val processingDialog= context?.let {
                            MaterialAlertDialogBuilder(it).setView(R.layout.layout_loading)
                                    .setBackground(ResourcesCompat.getDrawable(resources,R.drawable.bg_transparent,null))
                                    .create()
                        }
        processingDialog?.setCancelable(false)
        processingDialog?.show()
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    processingDialog?.dismiss()
                    Toast.makeText(context,"Reset link sent to your given email, Check your email inbox.",Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }else{
                    processingDialog?.dismiss()
                    Toast.makeText(context,task.exception?.message.toString(),Toast.LENGTH_LONG).show()
                }
            }
    }


}