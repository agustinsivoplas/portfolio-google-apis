package com.uy.atomico.googleapisportfolio.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.updateMargins
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.fragment_auth.*

/**
 * Created by agustin.sivoplas@gmail.com on 8/18/18.
 * Atomico Labs
 */
class FirebaseAuthActivity : BaseActivity() {

    companion object {
        const val TAG = "FirebaseAuthActivity"

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, FirebaseAuthActivity::class.java))
        }
    }

    override fun getLayoutResId() = R.layout.activity_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolbar()
        bindListeners()
    }

    private fun setUpToolbar() {
        toolbar.title = ""
        authMainLayout.setOnApplyWindowInsetsListener { _, windowInsets ->
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

        mAuth.addAuthStateListener {
            updateUI()
        }
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    private fun updateUI() {
        if (mAuth.currentUser != null) {
            setFragment(AuthSuccessFragment.newInstance(), AuthSuccessFragment.TAG, false, R.id.fragmentContainerAuth)
        } else {
            setFragment(FirebaseAuthFragment.newInstance(), FirebaseAuthFragment.TAG, false, R.id.fragmentContainerAuth)
        }
    }
}
