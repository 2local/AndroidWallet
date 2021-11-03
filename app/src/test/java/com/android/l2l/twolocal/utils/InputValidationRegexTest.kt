package com.android.l2l.twolocal.utils

import android.content.Context
import com.android.l2l.twolocal.ui.MainActivity
import com.google.common.truth.Truth
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class InputValidationRegexTest{


    @Before
    fun setUp() {
    }

    @Test
    fun isMobileValid() {
        val result1 = InputValidationRegex.isMobileValid("09138970887")
        Truth.assertThat(result1).isEqualTo(true)

        val result2 = InputValidationRegex.isMobileValid("9138970887")
        Truth.assertThat(result2).isEqualTo(true)

        val result3 = InputValidationRegex.isMobileValid("091389708")
        Truth.assertThat(result3).isEqualTo(false)

        val result4 = InputValidationRegex.isMobileValid("")
        Truth.assertThat(result4).isEqualTo(false)
    }


    @Test
    fun isMobilePassword() {
        val result1 = InputValidationRegex.isValidPassword("awertw534")
        Truth.assertThat(result1).isEqualTo(true)

        val result2 = InputValidationRegex.isValidPassword("3425564yrty")
        Truth.assertThat(result2).isEqualTo(true)

        val result3 = InputValidationRegex.isValidPassword("5545t")
        Truth.assertThat(result3).isEqualTo(false)

        val result4 = InputValidationRegex.isValidPassword("")
        Truth.assertThat(result4).isEqualTo(false)
    }

    @Test
    fun isMobileEmail() {
        val result1 = InputValidationRegex.isValidateEmail("email@gmail.com")
        Truth.assertThat(result1).isEqualTo(true)

        val result2 = InputValidationRegex.isValidateEmail("emai3w.ll@yahoo.com")
        Truth.assertThat(result2).isEqualTo(true)

        val result3 = InputValidationRegex.isValidateEmail("emai3w.llyahoo.com")
        Truth.assertThat(result3).isEqualTo(false)

        val result5 = InputValidationRegex.isValidateEmail("emai3w.ll@yahoocom")
        Truth.assertThat(result5).isEqualTo(false)

        val result4 = InputValidationRegex.isValidateEmail("@yahoocom")
        Truth.assertThat(result4).isEqualTo(false)

        val result6 = InputValidationRegex.isValidateEmail("")
        Truth.assertThat(result6).isEqualTo(false)

        val result7 = InputValidationRegex.isValidateEmail("erfan@gmail.c")
        Truth.assertThat(result7).isEqualTo(false)
    }

    @Test
    fun isValidateUsername() {
        val result1 = InputValidationRegex.isValidateUsername("emailGmailcom")
        Truth.assertThat(result1).isEqualTo(true)

        val result2 = InputValidationRegex.isValidateUsername("emai3wl76hoocom")
        Truth.assertThat(result2).isEqualTo(true)

        val result3 = InputValidationRegex.isValidateUsername("emcomee")
        Truth.assertThat(result3).isEqualTo(false)

        val result5 = InputValidationRegex.isValidateUsername("emai3w.llyocom")
        Truth.assertThat(result5).isEqualTo(false)

        val result4 = InputValidationRegex.isValidateUsername("@yahoocom")
        Truth.assertThat(result4).isEqualTo(false)

        val result6 = InputValidationRegex.isValidateUsername("")
        Truth.assertThat(result6).isEqualTo(false)

        val result7 = InputValidationRegex.isValidateUsername("GTr5saf33FD")
        Truth.assertThat(result7).isEqualTo(true)

        val result8 = InputValidationRegex.isValidateUsername("1aaaaasdfghjj")
        Truth.assertThat(result8).isEqualTo(false)

        val result9 = InputValidationRegex.isValidateUsername("zxcvbnma")
        Truth.assertThat(result9).isEqualTo(true)
    }
}