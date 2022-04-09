package two.local.a2local

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.android.l2l.twolocal.ui.MainActivity
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.PriceFormatUtils
import com.google.common.truth.Truth

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class CommonUtilsInstrumentedTest {

    lateinit var context: Context

    @Before
    @Throws(Exception::class)
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("two.local.io", appContext.packageName)
    }

    @Test
    fun encodeBase64ToString() {
        val result1 = CommonUtils.encodeBase64ToString("Erfan Eghterafi")
        Truth.assertThat(result1).isEqualTo("RXJmYW4gRWdodGVyYWZp")
    }

    @Test
    fun decodeBase64ToString() {
        val result1 = CommonUtils.decodeBase64ToString("RXJmYW4gRWdodGVyYWZp")
        Truth.assertThat(result1).isEqualTo("Erfan Eghterafi")
    }
}