package com.uy.atomico.googleapisportfolio.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.jakewharton.rxbinding2.widget.RxTextView
import com.uber.autodispose.autoDisposable
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.base.BaseFragment
import com.uy.atomico.googleapisportfolio.extensions.hideKeyboard
import com.uy.atomico.googleapisportfolio.extensions.value
import com.uy.atomico.googleapisportfolio.utils.KeyboardUtil
import com.uy.atomico.googleapisportfolio.utils.Validators
import kotlinx.android.synthetic.main.fragment_auth.*

/**
 * Created by agustin.sivoplas@gmail.com on 8/26/18.
 * Atomico Labs
 */

class FirebaseAuthFragment : BaseFragment() {

    companion object {
        const val TAG = "FirebaseAuthFragment"
        const val RC_SIGN_IN_CODE = 999

        fun newInstance(): FirebaseAuthFragment {
            return FirebaseAuthFragment()
        }
    }

    override fun getLayoutResId() = R.layout.fragment_auth

    private val mCallbackManager = CallbackManager.Factory.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KeyboardUtil(activity!!, view)
        bindListeners()
    }

    private fun bindListeners() {
        signUpButton.setOnClickListener { activity?.let { RegisterActivity.startActivity(it) } }
        loginButton.setOnClickListener { onLoginClicked() }
        googleSignInButton.setOnClickListener { startActivityForResult(mGoogleSignInClient.signInIntent, RC_SIGN_IN_CODE) }
        facebookSignInButton.setOnClickListener { LoginManager.getInstance().logInWithReadPermissions(this, arrayListOf("public_profile", "email")); }
        phoneSignInButton.setOnClickListener { activity?.let { PhoneAuthActivity.startActivity(it) } }
        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        firebaseAuthWithFacebook(loginResult.accessToken)
                    }

                    override fun onCancel() {
                    }

                    override fun onError(exception: FacebookException) {
                        Snackbar.make(authLayout, getString(R.string.facebook_authentication_failed) + exception.message, Snackbar.LENGTH_LONG).show()
                    }
                })

        RxTextView.textChanges(emailEditText)
                .skip(2)
                .autoDisposable(scopeProvider)
                .subscribe {
                    validateEmailInput(Validators.validateEmail(emailEditText.value()))
                }

        RxTextView.textChanges(passwordEditText)
                .skip(2)
                .autoDisposable(scopeProvider)
                .subscribe {
                    validatePasswordInput(Validators.validatePassword(passwordEditText.value()))
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e(FirebaseAuthActivity.TAG, "Google sign in failed", e)
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        activity?.let {
            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(it) { task ->
                        if (!task.isSuccessful) {
                            Snackbar.make(authLayout, getString(R.string.google_authentication_failed) + task.exception?.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
        }
    }

    private fun firebaseAuthWithFacebook(accessToken: AccessToken) {
        activity?.let {
            val credential = FacebookAuthProvider.getCredential(accessToken.token)
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(it) { task ->
                        if (!task.isSuccessful) {
                            Snackbar.make(authLayout, getString(R.string.facebook_authentication_failed) + task.exception?.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
        }
    }

    private fun onLoginClicked() {
        activity?.let {
            if (!Validators.validateEmail(emailEditText.value())) {
                validateEmailInput(false)
            } else {
                validateEmailInput(true)
                if (!Validators.validatePassword(passwordEditText.value())) {
                    validatePasswordInput(false)
                } else {
                    validatePasswordInput(true)
                    hideKeyboard()
                    loginButton.isRefreshing = true
                    mAuth.signInWithEmailAndPassword(emailEditText.value(), passwordEditText.value())
                            .addOnCompleteListener(it) { task ->
                                loginButton.isRefreshing = false
                                if (!task.isSuccessful) {
                                    Snackbar.make(authLayout, getString(R.string.authentication_failed) + task.exception?.message, Snackbar.LENGTH_LONG).show()
                                }
                            }
                }
            }
        }
    }

    private fun validateEmailInput(valid: Boolean) {
        if (valid) {
            if (!emailEditText.value().isNullOrBlank()) {
                val tickDrawable = VectorDrawableCompat.create(resources, R.drawable.ic_check_circle, null)
                emailEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, tickDrawable, null)
            }
            emailTextInputLayout.error = ""
        } else {
            emailTextInputLayout.error = getString(R.string.input_email_error)
            emailEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
        emailTextInputLayout.isErrorEnabled = !valid
    }

    private fun validatePasswordInput(valid: Boolean) {
        if (valid) {
            passwordTextInputLayout.error = ""
        } else {
            passwordTextInputLayout.error = getString(R.string.input_password_error)
        }
        passwordTextInputLayout.isErrorEnabled = !valid
    }
}