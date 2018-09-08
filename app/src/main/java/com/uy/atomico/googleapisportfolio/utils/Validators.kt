package com.uy.atomico.googleapisportfolio.utils

import java.util.regex.Pattern

/**
 * Created by agustin.sivoplas@gmail.com on 9/7/18.
 * Atomico Labs
 */

object Validators {

    private const val EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    fun validateEmail(email: String): Boolean {
        if(email.isNullOrBlank()) {
            return false;
        }

        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.isNotBlank() && password.length > 5
    }
}