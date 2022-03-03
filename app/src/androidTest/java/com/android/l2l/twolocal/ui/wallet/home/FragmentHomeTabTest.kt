package com.android.l2l.twolocal.ui.wallet.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.ui.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.AdditionalMatchers.not
import org.mockito.Mockito.mock
import kotlin.contracts.ExperimentalContracts

@MediumTest
@ExperimentalContracts
@RunWith(AndroidJUnit4ClassRunner::class)
class FragmentHomeTabTest {

    lateinit var context: Context

    @Before
    @Throws(Exception::class)
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun navv() {
        val mockNavController = mock(NavHostController::class.java)
        val fragmentHomeTab = launchFragmentInContainer<FragmentHomeTab>(Bundle(), R.style.AppTheme)

        fragmentHomeTab.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        onView(withId(R.id.text_total_balance)).check(matches(isDisplayed()))
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.txtTotalBalance)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerViewWallets)).check(matches(isDisplayed()))
        onView(withId(R.id.text_transactions)).check(matches(isDisplayed()))
        onView(withId(R.id.ic_setting)).check(matches(isDisplayed()))

        onView(withId(R.id.txt_title)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.ic_menu)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.ic_close)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.img_back)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.text_total_balance)).check(matches(withText(context.getString(R.string.home_total_balance))));
    }

}































