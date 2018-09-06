package com.uy.atomico.googleapisportfolio.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by agustin.sivoplas@gmail.com on 8/26/18.
 * Atomico Labs
 */

abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun getLayoutResId(): Int

    private var mHandler: Handler = Handler()

    protected val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    protected val mCredentialsApiClient: GoogleApiClient by lazy {
        GoogleApiClient.Builder(this@BaseActivity)
                .addApi(Auth.CREDENTIALS_API)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
    }

    override fun finish() {
        super.finish()
        //overridePendingTransition(R.anim.left_in, R.anim.right_out)
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
       // overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //overridePendingTransition(R.anim.left_in, R.anim.right_out)
    }

    protected fun setFragment(fragment: BaseFragment, tag: String, addToBackStack: Boolean, containerId: Int) {
        val mPendingRunnable = Runnable {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            //fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(tag)
            }
            fragmentTransaction.replace(containerId, fragment, tag)
            fragmentTransaction.commitAllowingStateLoss()
        }

        mHandler.post(mPendingRunnable)
    }
}