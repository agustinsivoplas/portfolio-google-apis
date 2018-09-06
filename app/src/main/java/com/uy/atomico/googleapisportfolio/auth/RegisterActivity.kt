package com.uy.atomico.googleapisportfolio.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.updateMargins
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.base.BaseActivity
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
            onBackPressed()
        }

    }
}
