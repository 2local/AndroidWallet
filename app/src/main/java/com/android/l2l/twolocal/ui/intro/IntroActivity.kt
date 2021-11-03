package com.android.l2l.twolocal.ui.intro;

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.l2l.twolocal.ui.authentication.AuthenticationActivity
import com.android.l2l.twolocal.ui.authentication.securityPassword.SecurityPasswordActivity
import com.android.l2l.twolocal.ui.intro.fragment.SliderOne
import com.android.l2l.twolocal.ui.intro.fragment.SliderThree
import com.android.l2l.twolocal.ui.intro.fragment.SliderTwo
import com.github.appintro.AppIntro2
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class IntroActivity: AppIntro2() {

    companion object {

        fun start(context: Context) {
            Intent(context, IntroActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(SliderOne.newInstance())
        addSlide(SliderTwo.newInstance())
        addSlide(SliderThree.newInstance())

        showStatusBar(false)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        Intent(this, AuthenticationActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        Intent(this, AuthenticationActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

}
