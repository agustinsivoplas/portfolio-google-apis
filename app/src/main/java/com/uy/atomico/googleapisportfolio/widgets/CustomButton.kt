package com.uy.atomico.googleapisportfolio.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.extensions.px

/**
 * Created by agustin.sivoplas@gmail.com on 9/10/18.
 * Atomico Labs
 */

class CustomButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    var isRefreshing: Boolean = false
        set(value) {
            field = value
            isClickable = !value
            if (value) {
                val rotateIcon = ContextCompat.getDrawable(context, R.drawable.progress_drawable)
                rotateIcon?.let {
                    iconRefreshingColor?.let { color ->
                       iconTint = ColorStateList.valueOf(ContextCompat.getColor(context, color))
                    }
                    icon = it
                    iconSize = 24.px
                    (it as AnimatedVectorDrawable).start()
                }
            } else {
                icon = null
            }
        }

    var iconRefreshingColor: Int? = null
}