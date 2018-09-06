package com.uy.atomico.googleapisportfolio.utils

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver

/**
 * This class implements a hack to change the layout padding on bottom if the keyboard is shown
 * to allow long lists with editTextViews
 */
class KeyboardUtil(act: Activity, private val contentView: View) {

    private val decorView: View = act.window.decorView

    //a small helper to allow showing the editText focus
    private var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val r = Rect()
        //r will be populated with the coordinates of your view that area still visible.
        decorView.getWindowVisibleDisplayFrame(r)

        //get screen height and calculate the difference with the useable area from the r
        val height = decorView.context.resources.displayMetrics.heightPixels
        val diff = height - r.bottom

        //if it could be a keyboard add the padding to the view
        if (diff != 0) {
            // if the use-able screen height differs from the total screen height we assume that it shows a keyboard now
            //check if the padding is 0 (if yes set the padding for the keyboard)
            if (contentView.paddingBottom != diff) {
                //set the padding of the contentView for the keyboard
                contentView.setPadding(0, 0, 0, diff)
            }
        } else {
            //check if the padding is != 0 (if yes reset the padding)
            if (contentView.paddingBottom != 0) {
                //reset the padding of the contentView
                contentView.setPadding(0, 0, 0, 0)
            }
        }
    }

    init {
        //only required on newer android versions. it was working on API level 19
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

    fun enable() {
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

    fun disable() {
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }
}