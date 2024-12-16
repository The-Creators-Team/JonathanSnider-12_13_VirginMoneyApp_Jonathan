package com.example.jonathansnidervirginmoney.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.jonathansnidervirginmoney.R
import com.example.jonathansnidervirginmoney.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    val provider = OAuthProvider.newBuilder("github.com")

    //used by Firebase for login functionality
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentLoginBinding? = null

    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN: Int = 1231


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
        val forgotPasswordButton = binding.forgetPasswordButton
        val loginGoogleButton = binding.loginGoogleButton
        val loginGithubButton = binding.loginGithubButton

        // Initialize Firebase Auth
        auth = Firebase.auth
        setupGoogleAuth()

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
            verifyFirebaseUser(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        registerButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_login_to_navigation_register, null,
                NavOptions.Builder().build()
            )
        }

        forgotPasswordButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_login_to_navigation_forgot_password, null,
                NavOptions.Builder().build()
            )
        }

        testButton.setOnClickListener {
            Toast.makeText(context, "${auth.currentUser?.email}", Toast.LENGTH_LONG).show()
        }

        loginGoogleButton.setOnClickListener {
            val signIn = googleSignInClient.signInIntent
            startActivityForResult(
                signIn, RC_SIGN_IN
            )
        }

        loginGithubButton.setOnClickListener {
            setUpGithubAuth()
        }


    }

    private fun setupGoogleAuth() {
        googleSignInClient = GoogleSignIn.getClient(
            requireContext(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()
        )
    }

    private fun setUpGithubAuth() {
        val pendingResultTask = auth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener { task ->
                    // User is signed in.
                    // IdP data available in
                    // authResult.getAdditionalUserInfo().getProfile().
                    // The OAuth access token can also be retrieved:
                    // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                    // The OAuth secret can be retrieved by calling:
                    // ((OAuthCredential)authResult.getCredential()).getSecret().
                    val user = auth.currentUser
                    Log.d("User", user?.email.toString())
                    Toast.makeText(context, "Good Login by ${user?.email}", Toast.LENGTH_LONG).show()
                    findNavController().navigate(
                        R.id.action_navigation_login_to_navigation_home, null,
                        NavOptions.Builder().build()
                    )


                }
                .addOnFailureListener { task ->
                    // Handle failure.
                    Toast.makeText(context, "Bad Login: ${task.message}", Toast.LENGTH_LONG)
                        .show()
                }
        } else {
            // There's no pending result so you need to start the sign-in flow.
            signInWithProvider(provider)
        }
    }

    fun signInWithProvider(provider: OAuthProvider.Builder) {
        // [START auth_oidc_provider_signin]
        auth.startActivityForSignInWithProvider(requireActivity(), provider.build())
            .addOnSuccessListener { task->
                // User is signed in.
                // IdP data available in
                // authResult.getAdditionalUserInfo().getProfile().
                // The OAuth access token can also be retrieved:
                // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                // The OAuth secret can be retrieved by calling:
                // ((OAuthCredential)authResult.getCredential()).getSecret().
                val user = auth.currentUser
                Log.d("User", user?.email.toString())
                Toast.makeText(context, "Good Login by ${user?.email}", Toast.LENGTH_LONG).show()
                findNavController().navigate(
                    R.id.action_navigation_login_to_navigation_home, null,
                    NavOptions.Builder().build()
                )

            }
            .addOnFailureListener { task->
                // Handle failure.
                Toast.makeText(context, "Bad Login: ${task.message}", Toast.LENGTH_LONG)
                    .show()
            }
        // [END auth_oidc_provider_signin]
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
                    //R.id.action_navigation_login_to_navigation_home, null,
                    //R.id.action_navigation_login_to_navigation_room_recycler_list,
                    R.id.action_navigation_login_to_navigation_user_recycler_list,
                    null,
                    NavOptions.Builder().build()
                )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Log.d("User", user?.email.toString())
                Toast.makeText(context, "Good Login by ${user?.email}", Toast.LENGTH_LONG).show()

                findNavController().navigate(
                    R.id.action_navigation_login_to_navigation_home, null,
                    NavOptions.Builder().build()
                )

            } else {
                Log.d("User", task.exception?.message.toString())
            }
        }
    }
}