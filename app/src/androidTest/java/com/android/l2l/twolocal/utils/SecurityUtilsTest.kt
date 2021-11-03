package com.android.l2l.twolocal.utils;

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.android.l2l.twolocal.ui.MainActivity
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class SecurityUtilsTest {

    lateinit var context: Context

    @Before
    @Throws(Exception::class)
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }


    @Test
    fun saveSecureString() {
        val someText = "some text for encryption"
        val someAlias = "alias"
//        "BKor2MaF/sPNm3a680Xy3A==\n]PwJPp2ZXwkb/SUoDHZobtThc26Ivr0rqYMqUzzIhWn0=\n"
        val secureString = SecurityUtils.saveSecureString(someText, someAlias, context)
        val originalString = SecurityUtils.getSecureString(secureString, someAlias, context)
        Truth.assertThat(originalString).isEqualTo(someText)
    }
}