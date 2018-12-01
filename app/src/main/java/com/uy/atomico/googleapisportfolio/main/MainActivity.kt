package com.uy.atomico.googleapisportfolio.main

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.auth.FirebaseAuthActivity
import com.uy.atomico.googleapisportfolio.base.BaseActivity
import com.uy.atomico.googleapisportfolio.data.api.PushAPI
import com.uy.atomico.googleapisportfolio.maps.MapsActivity
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.motion_main_drawer_layout_menu.*

/**
 * Created by agustin.sivoplas@gmail.com on 8/9/18.
 * Atomico Labs
 */
class MainActivity : BaseActivity() {

    override fun getLayoutResId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAtomicoLabsAnimation()
        bindListeners()
    }

    private fun bindListeners() {
        firebaseAuthTextView.setOnClickListener {
            FirebaseAuthActivity.startActivity(this)
            closeDrawer()
        }
        mapsTextView.setOnClickListener {
            MapsActivity.startActivity(this)
            closeDrawer()
        }
        driveTextView.setOnClickListener {
            closeDrawer()
        }
        visionTextView.setOnClickListener {
            closeDrawer()
        }
        messageTextView.setOnClickListener {
            PushAPI.sendPushNotification()
            closeDrawer()
        }
    }

    private fun startAtomicoLabsAnimation() {
        val alpha = AlphaAnimation(0f, 1f)
        alpha.duration = 2000
        alpha.fillAfter = true
        atomicoLabsTextView.startAnimation(alpha)
        welcomeImageView.startAnimation(alpha)

        val translateAnimation = TranslateAnimation(0f, 0f,
                resources.displayMetrics.heightPixels.toFloat(), 0f)
        translateAnimation.duration = 500
        translateAnimation.fillAfter = true
        backgroundMainImageView.startAnimation(translateAnimation)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun closeDrawer() {
        menuMotionLayout.postDelayed({
            menuMotionLayout.closeDrawer()
        }, 100)
    }
}
