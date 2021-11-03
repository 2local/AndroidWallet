package com.android.l2l.twolocal.ui.setting

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.getBackStackData
import com.android.l2l.twolocal.common.invisible
import com.android.l2l.twolocal.common.visible
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentSettingsBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.ui.authentication.AuthenticationActivity
import com.android.l2l.twolocal.ui.authentication.securityPassword.SecurityPasswordActivity
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.setting.di.DaggerSettingComponent
import com.android.l2l.twolocal.ui.splash.SplashActivity
import com.android.l2l.twolocal.utils.constants.AppConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingFragment : BaseFragment<SettingViewModel>(R.layout.fragment_settings) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: SettingViewModel by viewModels { viewModelFactory }
    private val binding: FragmentSettingsBinding by viewBinding(FragmentSettingsBinding::bind)

    companion object {
        const val CURRENCY_KEY = "CURRENCY_KEY"
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerSettingComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.logoutLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    SplashActivity.start(requireContext())
                }
                is ViewState.Error -> {
                    onMessageToast(it.error)
                }
                else -> {
                }
            }
        })

        viewModel.currencyTypeLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    binding.textCurrency.text = it.response.myName
                }
                else -> {
                }
            }
        })

        setupNavigation()

        binding.toolbar.getBackBtn().setOnClickListener {
            requireActivity().finish()
        }

        viewModel.getCurrency()

        getBackStackData<String>(CURRENCY_KEY) { data ->
            viewModel.getCurrency()
        }


    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isUserLoggedIn())
            binding.signOut.visible()
        else
            binding.signOut.invisible()
    }

    fun setupNavigation(){
        binding.accountInfo.setOnClickListener {
            if (viewModel.isUserLoggedIn())
                findNavController().navigate(R.id.action_settingScreen_to_accountInfoFragment)
            else
                AuthenticationActivity.start(requireContext())
        }

        binding.addressBook.setOnClickListener {
            findNavController().navigate(R.id.action_settingScreen_to_contactFragment)
        }
        binding.currency.setOnClickListener {
//            findNavController().navigate(R.id.action_settingScreen_to_currencyFragment)
        }

        binding.affiliate.setOnClickListener {
            if (viewModel.isUserLoggedIn())
                findNavController().navigate(R.id.action_settingScreen_to_affiliateFragment)
            else
                AuthenticationActivity.start(requireContext())
        }

        binding.password.setOnClickListener {
            findNavController().navigate(R.id.action_settingScreen_to_changeLocalPasswordFragment)
//            findNavController().navigate(R.id.action_settingScreen_to_passwordFragment)
        }

        binding.about.setOnClickListener {
            findNavController().navigate(R.id.action_settingScreen_to_aboutFragment)
        }

        binding.helpSupport.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(AppConstants.LOCAL_WEBSITE)
                startActivity(this)
            }
        }

        binding.signOut.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.fragment_setting_sign_out))
                .setMessage(getString(R.string.fragment_setting_sign_out_alert))
                .setPositiveButton(
                    R.string.fragment_setting_btn_sign_out
                ) { dialog, which -> viewModel.resetWalletAndSignOut() }
                .setNegativeButton(R.string.fragment_setting_cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }

    }
}