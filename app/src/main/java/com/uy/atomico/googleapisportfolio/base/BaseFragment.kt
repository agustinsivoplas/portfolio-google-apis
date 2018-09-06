package com.uy.atomico.googleapisportfolio.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.uy.atomico.googleapisportfolio.R

/**
 * Created by agustin.sivoplas@gmail.com on 8/26/18.
 * Atomico Labs
 */

abstract class BaseFragment : Fragment() {

    protected abstract fun getLayoutResId(): Int

    protected val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    protected val mGoogleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(activity!!, gso)
    }

    protected val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }
}