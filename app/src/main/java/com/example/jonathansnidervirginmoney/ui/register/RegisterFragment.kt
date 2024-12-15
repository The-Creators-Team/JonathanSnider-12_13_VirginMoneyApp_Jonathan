package com.example.jonathansnidervirginmoney.ui.register

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.jonathansnidervirginmoney.R
import com.example.jonathansnidervirginmoney.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class RegisterFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegisterBinding? = null

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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText = binding.newUsername
        val passwordEditText = binding.newPassword
        val newAccountButton = binding.newAccountButton

        auth = Firebase.auth

        newAccountButton.setOnClickListener {
            createNewFirebaseUser(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

    }

    private fun createNewFirebaseUser(newEmail: String, newPassword: String) {
        auth.createUserWithEmailAndPassword(newEmail, newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(context, "New Account by ${user?.email}", Toast.LENGTH_LONG)
                        .show()
                    auth.signOut()
                    findNavController().navigate(
                        R.id.action_navigation_register_to_navigation_login, null,
                        NavOptions.Builder().build())

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.", Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}