package com.android.l2l.twolocal.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.invisible
import com.android.l2l.twolocal.common.visible
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.ActivitySplashBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.ui.MainActivity
import com.android.l2l.twolocal.ui.authentication.AuthenticationActivity
import com.android.l2l.twolocal.ui.authentication.securityPassword.SecurityPasswordActivity
import com.android.l2l.twolocal.ui.base.BaseActivity
import com.android.l2l.twolocal.ui.intro.IntroActivity
import com.android.l2l.twolocal.ui.splash.di.DaggerSplashComponent
import com.android.l2l.twolocal.ui.splash.viewModel.AppInitState
import com.android.l2l.twolocal.ui.splash.viewModel.SplashViewModel
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchReferralInitListener
import io.branch.referral.BranchError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SplashActivity : BaseActivity(), BranchReferralInitListener {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private val binding: ActivitySplashBinding by viewBinding(ActivitySplashBinding::inflate)
    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    companion object {

        fun start(context: Context) {
            Intent(context, SplashActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerSplashComponent.factory().create(findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)

        viewModel.appInitLiveData.observe(this, {
            when (it) {
                is AppInitState.StartIntro -> {
                    IntroActivity.start(this@SplashActivity)
                    finish()
                }
                is AppInitState.StartLogin -> {
                    AuthenticationActivity.start(this@SplashActivity)
                }
                is AppInitState.StartMain -> {
                    MainActivity.start(this@SplashActivity)
                    finish()
                }
//                is AppInitState.StartSecurity -> {
//                    SecurityPasswordActivity.start(this@SplashActivity)
//                }
                else -> {
                }
            }
        })

        viewModel.cryptoPriceLiveData.observe(this, {
            when (it) {
                is ViewState.Loading -> {
                    binding.progress.visible()
                }
                is ViewState.Success -> {
                    binding.progress.invisible()
                    viewModel.initApp()
                }
                is ViewState.Error -> {
                    binding.progress.invisible()
                    Toast.makeText(this, getString(R.string.splash_there_is_server_side_problem), Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        })
        viewModel.getCryptoPrice()

    }

    override fun onStart() {
        super.onStart()
        Branch.sessionBuilder(this).withCallback(this).withData(if (intent != null) intent.data else null).init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        Branch.sessionBuilder(this).withCallback(this).reInit()
    }

    override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
        Timber.d("data from branch deep link: $referringParams")
    }
}