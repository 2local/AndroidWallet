package com.android.l2l.twolocal.utils

import android.util.Patterns
import java.util.regex.Pattern
import java.util.regex.Pattern.compile

object InputValidationRegex {

    // A placeholder mobile validation check
    fun isMobileValid(mobile: String?): Boolean {
        return if (mobile != null) mobile.length >= 10 else false
    }

    // A placeholder password validation check
    fun isValidPassword(target: String?): Boolean {
        return if (target.isNullOrBlank()) false else target.length >= 8
    }

    fun isValidateEmail(target: String?): Boolean {
        return if (target.isNullOrBlank()) false else compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}\$",
            Pattern.CASE_INSENSITIVE
        ).matcher(target).matches()
    }

    fun isValidateVerification(target: String?): Boolean {
        return !target.isNullOrBlank()
    }

    fun isValidateMobile(target: String?): Boolean {
        return if (target.isNullOrBlank()) {
            false
        } else {
            var ret = true
            if (target.length < 10) ret = false
            ret
        }
    }

    fun isValidatePassword(target: String?): Boolean {
        return if (target.isNullOrBlank()) {
            false
        } else {
            var ret = true
            if (target.length < 8) ret = false
            ret
        }
    }

    fun isValidateWalletNumber(target: String?): Boolean {
        return if (target.isNullOrBlank()) {
            false
        } else {
            val usernamePattern = compile("[A-Za-z0-9]{20,}")
            usernamePattern.matcher(target).matches()
        }
    }

    fun isValidateUsername(target: String?): Boolean {
        return if (target.isNullOrBlank()) {
            false
        } else {
            val usernamePattern = compile("[a-zA-Z]([a-zA-Z0-9]){7,20}")
            usernamePattern.matcher(target).matches()
        }
    }
}