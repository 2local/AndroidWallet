package com.android.l2l.twolocal.ui.authentication.securityPassword

import android.app.AlertDialog
import android.inputmethodservice.Keyboard.KEYCODE_DONE
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.Nullable
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.ui.authentication.di.DaggerAuthenticationComponent
import com.android.l2l.twolocal.ui.base.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.Executor
import javax.inject.Inject
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.common.hideKeyboard
import com.android.l2l.twolocal.databinding.FragmentUnlockAppBinding
import com.android.l2l.twolocal.ui.MainActivity
import com.android.l2l.twolocal.ui.authentication.securityPassword.viewmodel.SecurityFormState
import com.android.l2l.twolocal.ui.authentication.securityPassword.viewmodel.UnlockViewModel
import com.android.l2l.twolocal.ui.splash.SplashActivity
import com.android.l2l.twolocal.utils.setEditTextError

@ExperimentalCoroutinesApi
class FragmentUnlockApp : BaseFragment<UnlockViewModel>(R.layout.fragment_unlock_app) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: UnlockViewModel by viewModels { viewModelFactory }
    private val binding: FragmentUnlockAppBinding by viewBinding(FragmentUnlockAppBinding::bind)


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerAuthenticationComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.unlockAppState.observe(viewLifecycleOwner, {
            when (it) {
                is SecurityFormState.PasswordError -> {
                    binding.textPassword.setEditTextError(it.error)
                }
                is SecurityFormState.IsDataValid -> {
                    if (it.isValid) {
                        startMain()
                    }
                }
                else -> {
                }
            }
        })

        viewModel.resetWalletLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Success -> {
                    SplashActivity.start(requireContext())
//                    requireActivity().finish()
                }
                is ViewState.Error -> {
                    onMessageToast(it.error)
                }
                else -> {
                }
            }
        })

        binding.buttonLogin.setOnClickListener {
            val password = binding.textPassword.text.toString()
            viewModel.isValidForm(password, requireContext())
        }


        binding.lntTouchId.setOnClickListener {
            showBiometricLogin()
        }

        binding.btnResetWallet.setOnClickListener {
            resetWalletAlert()
        }

        binding.textPassword.setOnEditorActionListener { _, keyCode, _ ->
            if (keyCode == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                binding.buttonLogin.performClick()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    fun resetWalletAlert(){
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.fragment_unlock_app_alert_reset_wallet))
            .setMessage(getString(R.string.fragment_unlock_app_alert_reset_wallet_content))
            .setPositiveButton(
                R.string.fragment_unlock_app_alert_reset_submit
            ) { _, _ -> resetWalletConfirm() }
            .setNegativeButton(R.string.fragment_unlock_app_alert_reset_cancel, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun resetWalletConfirm(){
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.fragment_unlock_app_alert_reset_alert))
            .setMessage(getString(R.string.fragment_unlock_app_alert_reset_wallet_confirm))
            .setPositiveButton(
                R.string.fragment_unlock_app_alert_reset_submit
            ) { _, _ -> viewModel.resetWallet() }
            .setNegativeButton(R.string.fragment_unlock_app_alert_reset_cancel, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    override fun onResume() {
        super.onResume()
        val shouldCreatePassword: Boolean = viewModel.shouldCreatePassword()
        if (shouldCreatePassword)
            findNavController().navigate(R.id.action_unlock_to_create_password)
        else {
            val isTouchIDActive: Boolean = viewModel.isTouchIDActive()
            if (isTouchIDActive)
                showBiometricLogin()
        }
    }


    private fun startMain() {
        MainActivity.start(requireContext())
        requireActivity().finish()
    }

    private fun showBiometricLogin() {
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricManager = BiometricManager.from(requireContext())

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                authUser(executor)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_msg_no_biometric_hardware),
                    Toast.LENGTH_LONG
                ).show()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_msg_biometric_hw_unavailable),
                    Toast.LENGTH_LONG
                ).show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_msg_biometric_not_setup),
                    Toast.LENGTH_LONG
                ).show()
        }
    }


    private fun authUser(executor: Executor) {
        val promptInfo = PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_login_login))
            .setSubtitle(getString(R.string.biometric_login_using_biometric_credential))
            .setNegativeButtonText(getString(R.string.biometric_login_cancel))
            .build()


        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                startMain()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()

            }
        })

        biometricPrompt.authenticate(promptInfo)
    }

}