package com.uy.atomico.googleapisportfolio.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.updateMargins
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import com.uber.autodispose.autoDisposable
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.base.BaseActivity
import com.uy.atomico.googleapisportfolio.extensions.hideKeyboard
import com.uy.atomico.googleapisportfolio.extensions.value
import com.uy.atomico.googleapisportfolio.utils.KeyboardUtil
import com.uy.atomico.googleapisportfolio.utils.Validators
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_auth_register.*

/**
 * Created by agustin.sivoplas@gmail.com on 8/26/18.
 * Atomico Labs
 */
class RegisterActivity : BaseActivity() {

    companion object {
        const val TAG = "RegisterActivity"

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }
    }

    override fun getLayoutResId() = R.layout.activity_auth_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KeyboardUtil(this, registerLayout)
        setUpToolbar()
        bindListeners()
    }

    private fun setUpToolbar() {
        toolbar.title = ""
        registerLayout.setOnApplyWindowInsetsListener { _, windowInsets ->
            (toolbar.layoutParams as ViewGroup.MarginLayoutParams).updateMargins(top = windowInsets.systemWindowInsetTop)
            windowInsets
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun bindListeners() {
        toolbar.setNavigationOnClickListener {
            hideKeyboard()
            onBackPressed()
        }

        RxTextView.textChanges(emailEditText)
                .skip(2)
                .autoDisposable(scopeProvider)
                .subscribe {
                    validateEmailInput(Validators.validateEmail(emailEditText.value()))
                }

        val passwordObservable: Observable<CharSequence> = RxTextView.textChanges(passwordEditText).skip(2)
        passwordObservable.autoDisposable(scopeProvider)
                .subscribe {
                    validatePasswordInput(Validators.validatePassword(passwordEditText.value()), passwordTextInputLayout)
                }

        Observable.combineLatest(
                passwordObservable,
                RxTextView.textChanges(confirmPasswordEditText).skip(1),
                BiFunction { password: CharSequence, confirmPassword: CharSequence -> password.toString() == confirmPassword.toString() })
                .autoDisposable(scopeProvider).subscribe { validatePasswordsMatching(it) }


        registerButton.setOnClickListener { onCreateAccountClicked() }
    }

    private fun onCreateAccountClicked() {
        if (!Validators.validateEmail(emailEditText.value())) {
            validateEmailInput(false)
        } else {
            validateEmailInput(true)
            if (!Validators.validatePassword(passwordEditText.value())) {
                validatePasswordInput(false, passwordTextInputLayout)
            } else {
                validatePasswordInput(true, passwordTextInputLayout)

                if (!Validators.validatePassword(confirmPasswordEditText.value())) {
                    validatePasswordInput(false, confirmPasswordTextInputLayout)
                } else {
                    validatePasswordInput(true, confirmPasswordTextInputLayout)
                    if (passwordEditText.value() != confirmPasswordEditText.value()) {
                        validatePasswordsMatching(false)
                    } else {
                        validatePasswordsMatching(true)
                        hideKeyboard()
                        mAuth.createUserWithEmailAndPassword(emailEditText.value(), passwordEditText.value())
                                .addOnCompleteListener(this) { task ->
                                    if (!task.isSuccessful) {
                                        Snackbar.make(registerLayout, getString(R.string.could_not_create_account) + task.exception?.message, Snackbar.LENGTH_LONG).show()
                                    } else {
                                        finish()
                                    }
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

    private fun validatePasswordInput(valid: Boolean, inputLayout: TextInputLayout) {
        if (valid) {
            inputLayout.error = ""
        } else {
            inputLayout.error = getString(R.string.input_password_error)
        }
        inputLayout.isErrorEnabled = !valid
    }

    private fun validatePasswordsMatching(valid: Boolean) {
        if (valid) {
            confirmPasswordTextInputLayout.error = ""
        } else {
            confirmPasswordTextInputLayout.error = getString(R.string.input_password_dont_match_error)
        }
        confirmPasswordTextInputLayout.isErrorEnabled = !valid
    }
}
