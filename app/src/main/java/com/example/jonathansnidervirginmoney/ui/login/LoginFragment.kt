package com.example.jonathansnidervirginmoney.ui.login

import android.content.ContentValues.TAG
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.jonathansnidervirginmoney.databinding.FragmentLoginBinding

import com.example.jonathansnidervirginmoney.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    //used by Firebase for login functionality
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentLoginBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.loginButton
        val loadingProgressBar = binding.loading
        val registerButton = binding.registerButton
        val testButton = binding.testButton
        val forgotPasswordButton=binding.forgetPasswordButton
        // Initialize Firebase Auth
        auth = Firebase.auth

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            /* loginViewModel.login(
                 usernameEditText.text.toString(),
                 passwordEditText.text.toString()
             )*/

            verifyFirebaseUser(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        registerButton.setOnClickListener {

            findNavController().navigate(
                R.id.action_navigation_login_to_navigation_register, null,
                NavOptions.Builder().build())
        }

        forgotPasswordButton.setOnClickListener{
            findNavController().navigate(
                R.id.action_navigation_login_to_navigation_forgot_password, null,
                NavOptions.Builder().build())
        }

        testButton.setOnClickListener{
            Toast.makeText(context, "${auth.currentUser?.email}", Toast.LENGTH_LONG).show()
        }




    }


    private fun verifyFirebaseUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(context, "Good Login by ${user?.email}", Toast.LENGTH_LONG).show()

                //navigate to Home Page
               /* findNavController().navigate(
                    //ALWAYS NAVIGATE USING ACTIONS AS OPPOSED TO THE NAVIGATE IDS
                    //R.id.navigate_home < THIS IS BAD
                    R.id.action_navigation_login_to_navigation_home, null, NavOptions.Builder()
                        .setPopUpTo(
                            R.id.navigation_login,
                            true
                        ) // Set inclusive to true if you want to remove the point you've popped up to as well
                        .build()
                )*/

                findNavController().navigate(
                    R.id.action_navigation_login_to_navigation_home, null,
                    NavOptions.Builder().build())
            } else {
                Toast.makeText(context, "Bad Login: ${task.exception?.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }



    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}