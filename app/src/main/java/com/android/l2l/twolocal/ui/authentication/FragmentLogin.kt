package com.android.l2l.twolocal.ui.authentication

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
import com.android.l2l.twolocal.databinding.FragmentLoginBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.output.LoginOutput
import com.android.l2l.twolocal.ui.authentication.di.DaggerAuthenticationComponent
import com.android.l2l.twolocal.ui.authentication.viewModel.formState.LoginFormState
import com.android.l2l.twolocal.ui.authentication.viewModel.LoginViewModel
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.utils.setEditTextError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.Executor
import javax.inject.Inject
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.common.hideKeyboard
import com.android.l2l.twolocal.ui.MainActivity
import com.android.l2l.twolocal.ui.splash.SplashActivity

@ExperimentalCoroutinesApi
class FragmentLogin: BaseFragment<LoginViewModel>(R.layout.fragment_login) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: LoginViewModel  by viewModels {viewModelFactory}
    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerAuthenticationComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginFormState.observe(viewLifecycleOwner, {
            when (it) {
                is LoginFormState.UsernameError -> {
                    binding.textEmail.setEditTextError(it.error)
                }
                is LoginFormState.PasswordError -> {
                    binding.textPassword.setEditTextError(it.error)
                }
                else -> {
                }
            }
        })


        viewModel.loginLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    if (it.response.twoFaIsActive())
                        showTwoFactorDialog()
                    else {
                        MainActivity.start(requireContext())
                        requireActivity().finish()
                    }
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error)
                }
                else -> {
                }
            }
        })

        binding.buttonLogin.setOnClickListener {
            val username= binding.textEmail.text.toString()
            val password= binding.textPassword.text.toString()
            viewModel.loginUser(LoginOutput(username, password))
        }

        binding.textFingerprint.setOnClickListener {
            showBiometricLogin()
        }

        binding.loginSignUpLabel.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
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

    fun showTwoFactorDialog() {
        DialogTowFactorVerification.newInstance().show(childFragmentManager, "")
    }

    fun showBiometricLogin() {
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
                    viewModel.loginBiometric()
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