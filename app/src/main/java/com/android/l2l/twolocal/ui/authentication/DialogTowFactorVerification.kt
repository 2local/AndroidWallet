package com.android.l2l.twolocal.ui.authentication

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.DialogTwoFaConfirmationBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.ui.MainActivity
import com.android.l2l.twolocal.ui.authentication.di.DaggerAuthenticationComponent
import com.android.l2l.twolocal.ui.authentication.viewModel.formState.TwoFAFormState
import com.android.l2l.twolocal.ui.authentication.viewModel.TwoFactorViewModel
import com.android.l2l.twolocal.ui.base.BaseViewActions
import com.android.l2l.twolocal.ui.splash.SplashActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DialogTowFactorVerification : DialogFragment(R.layout.dialog_two_fa_confirmation) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    val viewModel: TwoFactorViewModel by viewModels {viewModelFactory}
    private val binding: DialogTwoFaConfirmationBinding by viewBinding(DialogTwoFaConfirmationBinding::bind)

    companion object {

        fun newInstance(): DialogTowFactorVerification {
            return DialogTowFactorVerification()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAuthenticationComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
       val viewAction = BaseViewActions.getInstance(requireContext())
        viewModel.loginFormState.observe(viewLifecycleOwner, {
            when (it) {
                is TwoFAFormState.InvalidCode -> {
                    viewAction.onMessageToast(it.error)
                }
                else -> {
                }
            }
        })


        viewModel.loginLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    viewAction.showLoading()
                }
                is ViewState.Success -> {
                    viewAction.hideLoading()
                    MainActivity.start(requireContext())
                    dismiss()
                    requireActivity().finish()

                }
                is ViewState.Error -> {
                    viewAction.hideLoading()
                    viewAction.onErrorDialog(it.error)
                }
                else -> {
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setupClickListeners() {
        binding.buttonConfirm.setOnClickListener {
            val code = binding.codeInputView.code
           viewModel.verifyUser(code)
        }
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

}