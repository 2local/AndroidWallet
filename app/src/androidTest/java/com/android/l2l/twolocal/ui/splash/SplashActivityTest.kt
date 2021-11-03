package com.android.l2l.twolocal.ui.splash


import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.ui.MainActivity
import com.android.l2l.twolocal.ui.intro.IntroActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class SplashActivityTest {

    lateinit var context: Context

//    @Rule
//    @JvmField
//    var mActivityTestRule = ActivityTestRule(IntroActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch(IntroActivity::class.java)
    }

    @Test
    fun splashActivityTest() {
        val appCompatImageButton = onView(
            allOf(
                withId(R.id.next), withContentDescription("NEXT"),
                childAtPosition(
                    allOf(
                        withId(R.id.background),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.next), withContentDescription("NEXT"),
                childAtPosition(
                    allOf(
                        withId(R.id.background),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val appCompatImageButton3 = onView(
            allOf(
                withId(R.id.done), withContentDescription("DONE"),
                childAtPosition(
                    allOf(
                        withId(R.id.background),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.txt_login),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText(context.getString(R.string.fragment_login_title))))
        textView.check(matches(isDisplayed()))

        val editText = onView(
            allOf(
                withId(R.id.text_email), hasTextInputLayoutHintText(context.getString(R.string.fragment_login_edt_email)),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        editText.check(matches(isDisplayed()))
        editText.check(matches(hasTextInputLayoutHintText("Email")))

        val editText3 = onView(
            allOf(
                withId(R.id.text_password), hasTextInputLayoutHintText(context.getString(R.string.fragment_login_password)),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        editText3.check(matches(isDisplayed()))
        editText3.check(matches(hasTextInputLayoutHintText(context.getString(R.string.fragment_login_password))))

        val imageButton = onView(
            allOf(
                withId(R.id.text_input_end_icon),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val button = onView(
            allOf(
                withId(R.id.button_login), withText(context.getString(R.string.fragment_login_btn_login)),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withId(R.id.image_fingerprint),
                withParent(withParent(withId(R.id.nav_host_authentication))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView3 = onView(
            allOf(
                withId(R.id.text_fingerprint), withText(context.getString(R.string.fragment_login_touch_the_fingerprint_sensor_to_login)),
                withParent(withParent(withId(R.id.nav_host_authentication))),
                isDisplayed()
            )
        )
        textView3.check(matches(isDisplayed()))
        textView3.check(matches(withText(context.getString(R.string.fragment_login_touch_the_fingerprint_sensor_to_login))))

    }

    fun hasTextInputLayoutHintText(expectedErrorText: String): Matcher<View> = object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description?) { }

        override fun matchesSafely(item: View?): Boolean {
            if (item is TextInputLayout) return false

            val error = (item as TextInputEditText).hint ?: return false
            val hint = error.toString()
            return expectedErrorText == hint
        }
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
