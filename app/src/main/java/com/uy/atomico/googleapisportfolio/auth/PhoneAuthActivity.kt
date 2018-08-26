package com.uy.atomico.googleapisportfolio.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateMargins
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.extensions.value
import kotlinx.android.synthetic.main.activity_auth_phone.*
import java.util.concurrent.TimeUnit

/**
 * Created by agustin.sivoplas@gmail.com on 8/25/18.
 * Atomico Labs
 */
class PhoneAuthActivity : AppCompatActivity() {

    companion object {
        const val TAG = "PhoneAuthActivity"
        const val RESOLVE_HINT = 998

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, PhoneAuthActivity::class.java))
        }
    }

    private val mCredentialsApiClient: GoogleApiClient by lazy {
        GoogleApiClient.Builder(this@PhoneAuthActivity)
                .addApi(Auth.CREDENTIALS_API)
                .build()
    }

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private var mVerificationId: String? = null
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val phoneCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
        }

        override fun onVerificationFailed(ex: FirebaseException) {
            Log.e(TAG, "onVerificationFailed:${ex.message}", ex)
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            Log.d(TAG, "onCodeSent:$verificationId")
            mVerificationId = verificationId
            mResendToken = token
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_phone)
        setUpToolbar()
        bindListeners()
        startBackgroundAnimationAnimation()
        requestHint()
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

        verifyCodeButton.setOnClickListener {
            signInWithPhoneAuth()
        }
    }

    private fun requestHint() {
        val hintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()
        val intent = Auth.CredentialsApi.getHintPickerIntent(mCredentialsApiClient, hintRequest)
        startIntentSenderForResult(intent.intentSender, RESOLVE_HINT, null, 0, 0, 0)
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
        if (requestCode == RESOLVE_HINT) {
            data?.extras?.let {
                if (it.containsKey("com.google.android.gms.credentials.Credential")) {
                    val phone = (it["com.google.android.gms.credentials.Credential"] as Credential).id
                    Log.d(TAG, "Request verification code for: $phone")
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, this, phoneCallback)
                }
            }
        }
    }

    private fun signInWithPhoneAuth() {
        if (!mVerificationId.isNullOrBlank() && !codeEditText.value().isNullOrBlank()) {
            val credential = PhoneAuthProvider.getCredential(mVerificationId!!, codeEditText.value())
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            finish()
                        } else {
                            Snackbar.make(phoneLayout, "Phone Authentication Failed: " + task.exception?.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
        }
    }
}