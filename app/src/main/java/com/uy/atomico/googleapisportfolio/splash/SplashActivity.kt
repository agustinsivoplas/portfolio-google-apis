package com.uy.atomico.googleapisportfolio.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.base.BaseActivity
import com.uy.atomico.googleapisportfolio.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by agustin.sivoplas@gmail.com on 8/11/18.
 * Atomico Labs
 */
class SplashActivity : BaseActivity() {

    override fun getLayoutResId() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashMotionLayout.transitionToEnd()

        splashMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                val atomPair = Pair.create<View, String>(atomSplashImageView, ViewCompat.getTransitionName(atomSplashImageView))
                val flaskPair = Pair.create<View, String>(flaskSplashImageView, ViewCompat.getTransitionName(flaskSplashImageView))
                val backgroundPair = Pair.create<View, String>(backgroundSplashImageView, ViewCompat.getTransitionName(backgroundSplashImageView))

                val pairs = ArrayList<Pair<View, String>>()
                pairs.add(atomPair)
                pairs.add(flaskPair)
                pairs.add(backgroundPair)

                var bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this@SplashActivity, *pairs.toTypedArray()).toBundle()
                splashMotionLayout.postDelayed({ startActivity(Intent(this@SplashActivity, MainActivity::class.java), bundle) }, 300)
            }
        })
    }
}
