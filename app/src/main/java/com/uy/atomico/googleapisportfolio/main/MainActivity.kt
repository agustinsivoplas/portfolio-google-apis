package com.uy.atomico.googleapisportfolio.main

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.data.api.PushAPI
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.motion_main_drawer_layout_menu.*

/**
 * Created by agustin.sivoplas@gmail.com on 8/9/18.
 * Atomico Labs
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startAtomicoLabsAnimation()
        bindListeners()
    }

    private fun bindListeners() {
        firebaseAuthTextView.setOnClickListener {  }
        mapsTextView.setOnClickListener {  }
        driveTextView.setOnClickListener {  }
        visionTextView.setOnClickListener {  }
        messageTextView.setOnClickListener {
            PushAPI.sendPushNotification()
            menuMotionLayout.closeDrawer()
        }
    }

    private fun startAtomicoLabsAnimation() {
        val alpha = AlphaAnimation(0f, 1f)
        alpha.duration = 2000
        alpha.fillAfter = true
        atomicoLabsTextView.startAnimation(alpha)
        welcomeTextView.startAnimation(alpha)

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
}