package com.uy.atomico.googleapisportfolio.auth

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.base.BaseFragment
import com.uy.atomico.googleapisportfolio.extensions.loadCircleImage
import com.uy.atomico.googleapisportfolio.extensions.spanBoldLastWord
import kotlinx.android.synthetic.main.fragment_auth_success.*

/**
 * Created by agustin.sivoplas@gmail.com on 8/26/18.
 * Atomico Labs
 */

class AuthSuccessFragment : BaseFragment() {

    companion object {
        const val TAG = "AuthSuccessFragment"

        fun newInstance(): AuthSuccessFragment {
            return AuthSuccessFragment()
        }
    }

    override fun getLayoutResId() = R.layout.fragment_auth_success

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindListeners()
    }

    private fun bindListeners() {
        signOutButton.setOnClickListener {
            signOut()
        }
    }

    override fun onStart() {
        super.onStart()
        renderUserData(mAuth.currentUser)
    }

    private fun signOut() {
        mAuth.signOut()
        mGoogleSignInClient.revokeAccess()
        LoginManager.getInstance().logOut()
    }

    private fun renderUserData(user: FirebaseUser?) {
        activity?.let {activity ->
            if (isAdded && !activity.isFinishing && user != null) {

                    user.displayName?.let {
                        displayNameTextView.text = it.spanBoldLastWord()
                    }

                    when (user.providers?.get(0)) {
                        GoogleAuthProvider.PROVIDER_ID -> {
                            user.photoUrl?.let {

                                authenticatedImageView.loadCircleImage(
                                        it.toString() + "?size=512",
                                        resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                        resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                        R.drawable.ic_account_circle
                                )
                            }
                            connectedWithTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(activity, R.drawable.google), null)
                        }

                        FacebookAuthProvider.PROVIDER_ID -> {
                            user.photoUrl?.let {

                                authenticatedImageView.loadCircleImage(
                                        it.toString() + "?width=512&heigth=512",
                                        resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                        resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                        R.drawable.ic_account_circle
                                )
                            }
                            connectedWithTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(activity, R.drawable.facebook_box), null)
                        }

                        PhoneAuthProvider.PROVIDER_ID -> {
                            connectedWithTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(activity, R.drawable.cellphone_lock), null)
                            user.photoUrl?.let {
                                authenticatedImageView.loadCircleImage(
                                        it.toString(),
                                        resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                        resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                        R.drawable.ic_account_circle
                                )
                            }

                            user.phoneNumber?.let {
                                displayNameTextView.text = "Last three digits ${it.takeLast(3)}".spanBoldLastWord()
                            }
                        }

                        else -> {
                            user.photoUrl?.let {
                                authenticatedImageView.loadCircleImage(
                                        it.toString(),
                                        resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                        resources.getDimensionPixelSize(R.dimen.authenticated_image_view_size),
                                        R.drawable.ic_account_circle
                                )
                            }

                            connectedWithTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(activity, R.drawable.ic_email), null)
                        }
                    }

                    if (!user.email.isNullOrBlank()) {
                        emailTextView.text = user.email
                        emailTextView.visibility = View.VISIBLE
                    } else {
                        emailTextView.visibility = View.GONE
                    }
            }
        }
    }
}