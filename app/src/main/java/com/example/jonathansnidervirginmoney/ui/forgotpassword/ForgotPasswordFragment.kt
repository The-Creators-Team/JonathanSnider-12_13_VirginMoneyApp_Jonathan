package com.example.jonathansnidervirginmoney.ui.forgotpassword

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.jonathansnidervirginmoney.R
import com.example.jonathansnidervirginmoney.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ForgotPasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentForgotPasswordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sendResetButton = binding.sendResetButton
        val accountEmail = binding.accountEmail

        auth = Firebase.auth

        sendResetButton.setOnClickListener {
            sendResetEmail(accountEmail.text.toString())
        }

    }

    private fun sendResetEmail(accountEmail: String) {
        auth.sendPasswordResetEmail(accountEmail).addOnCompleteListener {task->
            if (task.isSuccessful) {
                Toast.makeText(context, "Reset Email sent to: $accountEmail", Toast.LENGTH_LONG).show()

                findNavController().navigate(
                    R.id.action_navigation_forgot_password_to_navigation_login,
                    null,
                    NavOptions.Builder().build()
                )
            }
            else{
                Toast.makeText(context, "Failed to send reset email", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

}