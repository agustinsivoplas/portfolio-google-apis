package com.uy.atomico.googleapisportfolio.extensions

import android.content.Context
import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.uy.atomico.googleapisportfolio.R
import java.util.concurrent.TimeUnit

/**
 * Created by agustin.sivoplas@gmail.com on 8/15/18.
 * Atomico Labs
 */
fun AppCompatImageView.loadImage(url: String) {
    Glide.with(context)
            .load(url)
            .into(this)
}

fun AppCompatImageView.loadImage(url: String, width: Int, height: Int, placeholderRes: Int? = null) {
    val options = RequestOptions()
            .override(width, height)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    placeholderRes?.let {
        options.apply {
            placeholder(placeholderRes)
            error(placeholderRes)
        }
    }

    Glide.with(context)
            .load(url)
            .apply(options)
            .into(this)
}

fun AppCompatImageView.loadCircleImage(url: String, width: Int, height: Int, placeholderRes: Int? = null) {
    val options = RequestOptions()
            .override(width, height)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    placeholderRes?.let {
        options.apply {
            placeholder(placeholderRes)
            error(placeholderRes)
        }
    }

    Glide.with(context)
            .load(url)
            .apply(options)
            .into(this)
}

fun AppCompatImageView.loadImage(resId: Int) {
    Glide.with(context)
            .load(AppCompatResources.getDrawable(context, resId))
            .into(this)
}

fun AppCompatImageView.loadImage(resId: Int, width: Int, height: Int) {
    val options = RequestOptions()
            .override(width, height)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    Glide.with(context)
            .load(AppCompatResources.getDrawable(context, resId))
            .apply(options)
            .into(this)
}

fun AppCompatImageView.loadCircleImage(resId: Int, width: Int, height: Int) {
    val options = RequestOptions()
            .override(width, height)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    Glide.with(context)
            .load(AppCompatResources.getDrawable(context, resId))
            .apply(options)
            .into(this)
}

fun AppCompatImageView.loadRoundedCornersImage(resId: Int, width: Int, height: Int) {
    val options = RequestOptions()
            .override(width, height)
            .transforms(CenterCrop(), RoundedCorners(context.resources.getDimensionPixelSize(R.dimen.view_default_margin)))
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    Glide.with(context)
            .load(AppCompatResources.getDrawable(context, resId))
            .apply(options)
            .into(this)
}

fun AppCompatImageView.loadRoundedCornersImage(url: String, width: Int, height: Int, placeholderRes: Int? = null) {
    val options = RequestOptions()
            .override(width, height)
            .transforms(CenterCrop(), RoundedCorners(context.resources.getDimensionPixelSize(R.dimen.view_default_margin)))
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    placeholderRes?.let {
        options.apply {
            placeholder(placeholderRes)
            error(placeholderRes)
        }
    }

    Glide.with(context)
            .load(url)
            .apply(options)
            .into(this)
}

fun AppCompatActivity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun AppCompatActivity.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

fun Fragment.hideKeyboard() {
    activity?.let {
        val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.window.decorView.windowToken, 0)
    }
}

fun Fragment.showKeyboard() {
    activity?.let {
        val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }
}

fun AppCompatEditText.value(): String {
    return text.toString()
}

fun AppCompatAutoCompleteTextView.value(): String {
    return text.toString()
}

fun String.isNumeric(): Boolean =
        try {
            this.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.getCenter(): Pair<Float, Float> {
    val cx = this.x + this.width / 2
    val cy = this.y + this.height / 2
    return Pair(cx, cy)
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Double.formatToMoney(): String {
    return String.format("$%.2f", this)
}

fun Long.formatSecondsToHHmmss(): String {
    if (this < 60) {
        return "00:${String.format("%02d", this)}"
    }

    val hours = TimeUnit.SECONDS.toHours(this)
    val minutes = TimeUnit.SECONDS.toMinutes(this)
    val seconds = this % 60

    return return if (hours > 0) {
        "${String.format("%02d", hours)}:${String.format("%02d", minutes % 60)}:${String.format("%02d", seconds)}"
    } else {
        "${String.format("%02d", minutes)}:${String.format("%02d", seconds)}"
    }
}

fun String.spanBoldFirstWord(): SpannableStringBuilder {
    val span = SpannableStringBuilder(this)
    val firsSpace = this.indexOf(" ")
    if (firsSpace != -1) {
        span.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, firsSpace, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }
    return span
}

fun String.spanBoldLastWord(): SpannableStringBuilder {
    val span = SpannableStringBuilder(this)
    val lastSpace = this.lastIndexOf(" ")
    if (lastSpace != -1) {
        span.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), lastSpace, span.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    }
    return span
}

fun String.spanBold(start: Int, end: Int): SpannableStringBuilder {
    val span = SpannableStringBuilder(this)
    span.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    return span
}

fun String.spanBold(start: Int): SpannableStringBuilder {
    val span = SpannableStringBuilder(this)
    span.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, span.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    return span
}

fun View.resetFocus() {
    isFocusable = false
    isFocusableInTouchMode = false
    isFocusable = true
    isFocusableInTouchMode = true
}