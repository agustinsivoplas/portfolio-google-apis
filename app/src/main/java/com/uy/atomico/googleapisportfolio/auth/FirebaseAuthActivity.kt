package com.uy.atomico.googleapisportfolio.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateMargins
import com.decemberlabs.heroly.utils.extensions.loadCircleImage
import com.decemberlabs.heroly.utils.extensions.spanBoldLastWord
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.uy.atomico.googleapisportfolio.R
import kotlinx.android.synthetic.main.activity_auth.*


/**
 * Created by agustin.sivoplas@gmail.com on 8/18/18.
 * Atomico Labs
 */
class FirebaseAuthActivity : AppCompatActivity() {

    companion object {
        const val TAG = "FirebaseAuthActivity"
        const val RC_SIGN_IN_CODE = 999

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, FirebaseAuthActivity::class.java))
        }
    }


    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
    }

    private val mGoogleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(this, gso)
    }

    private val mCallbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setUpToolbar()

        bindListeners()
        startBackgroundAnimationAnimation()
    }

    private fun setUpToolbar() {
        toolbar.title = ""
        authLayout.setOnApplyWindowInsetsListener { _, windowInsets ->
            (toolbar.layoutParams as ViewGroup.MarginLayoutParams).updateMargins(top = windowInsets.systemWindowInsetTop)
            windowInsets
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun bindListeners() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        googleSignInButton.setOnClickListener { startActivityForResult(mGoogleSignInClient.signInIntent, RC_SIGN_IN_CODE) }
        facebookSignInButton.setOnClickListener { LoginManager.getInstance().logInWithReadPermissions(this, arrayListOf("public_profile", "email")); }

        signOutButton.setOnClickListener {
            signOut()
        }

        mAuth.addAuthStateListener {
            renderUserData(mAuth.currentUser)
        }

        LoginManager.getInstance().registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        firebaseAuthWithFacebook(loginResult.accessToken)
                    }

                    override fun onCancel() {
                    }

                    override fun onError(exception: FacebookException) {
                        Snackbar.make(authLayout, "Facebook Authentication Failed: " + exception.message, Snackbar.LENGTH_LONG).show()
                    }
                })
    }

    override fun onStart() {
        super.onStart()
        renderUserData(mAuth.currentUser)
    }

    private fun startBackgroundAnimationAnimation() {
        val translateAnimation = TranslateAnimation(0f, 0f,
                resources.getDimensionPixelSize(R.dimen.background_dashboard_height) * -1f, 0f)
        translateAnimation.duration = 500
        translateAnimation.fillAfter = true
        backgroundDashboardImageView.startAnimation(translateAnimation)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e(TAG, "Google sign in failed", e)
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        Snackbar.make(authLayout, "Authentication Failed: " + task.exception?.message, Snackbar.LENGTH_LONG).show()
                    }
                }
    }

    private fun firebaseAuthWithFacebook(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        Snackbar.make(authLayout, "Facebook Authentication Failed: " + task.exception?.message, Snackbar.LENGTH_LONG).show()
                    }
                }
    }

    private fun signOut() {
        mAuth.signOut()
        mGoogleSignInClient.revokeAccess()
        LoginManager.getInstance().logOut();
    }

    private fun renderUserData(user: FirebaseUser?) {
        if (!isFinishing) {
            if (user != null) {
                loginLayout.visibility = View.GONE
                authenticatedLayout.visibility = View.VISIBLE

                user.displayName?.let {
                    displayNameTextView.text = it.spanBoldLastWord()
                }

                user.photoUrl?.let {

                    when(user.providers?.get(0)) {
                        GoogleAuthProvider.PROVIDER_ID -> {
                            authenticatedImageView.loadCircleImage(
                                    it.toString() + "?size=512",
                                    resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                    resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                    R.drawable.ic_account_circle
                            )
                        }

                        FacebookAuthProvider.PROVIDER_ID -> {
                            authenticatedImageView.loadCircleImage(
                                    it.toString() + "?width=512&heigth=512",
                                    resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                    resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                    R.drawable.ic_account_circle
                            )
                        }

                        else -> {
                            authenticatedImageView.loadCircleImage(
                                    it.toString(),
                                    resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                    resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                    R.drawable.ic_account_circle
                            )
                        }
                    }
                }

                if (!user.email.isNullOrBlank()) {
                    emailTextView.text = user.email
                    emailTextView.visibility = View.VISIBLE
                } else {
                    emailTextView.visibility = View.GONE
                }

                if (!user.phoneNumber.isNullOrBlank()) {
                    phoneTextView.text = user.phoneNumber
                    phoneTextView.visibility = View.VISIBLE
                } else {
                    phoneTextView.visibility = View.GONE
                }

            } else {
                authenticatedLayout.visibility = View.GONE
                loginLayout.visibility = View.VISIBLE
            }
        }
    }
}
