package com.android.l2l.twolocal.ui.setting.localPassword

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.ui.base.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.common.hideKeyboard
import com.android.l2l.twolocal.databinding.FragmentChangeLocalPasswordBinding
import com.android.l2l.twolocal.ui.setting.di.DaggerSettingComponent
import com.android.l2l.twolocal.ui.setting.localPassword.viewModel.ChangeLocalPasswordViewModel
import com.android.l2l.twolocal.ui.setting.localPassword.viewModel.ChangePasswordFormState
import com.android.l2l.twolocal.utils.setEditTextError

@ExperimentalCoroutinesApi
class FragmentChangeLocalPassword: BaseFragment<ChangeLocalPasswordViewModel>(R.layout.fragment_change_local_password) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: ChangeLocalPasswordViewModel by viewModels {viewModelFactory}
    private val binding: FragmentChangeLocalPasswordBinding by viewBinding(FragmentChangeLocalPasswordBinding::bind)


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerSettingComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.createPasswordFormState.observe(viewLifecycleOwner, {
            when (it) {
                is ChangePasswordFormState.CurrentPasswordError -> {
                    binding.textCurrentPassword.setEditTextError(it.error)
                }
                is ChangePasswordFormState.NewPasswordError -> {
                    binding.textNewPassword.setEditTextError(it.error)
                }
                is ChangePasswordFormState.ConfirmPasswordError -> {
                    binding.textConfirmPassword.setEditTextError(it.error)
                }
                is ChangePasswordFormState.IsDataValid -> {
                    if(it.isValid)
                    {
                        onMessageToast(R.string.fragment_change_local_password_change_successful)
                        findNavController().popBackStack()
                    }
                }
                else -> {
                }
            }
        })


        binding.buttonChangePassword.setOnClickListener {
            changeMyPassword()
        }

        binding.textConfirmPassword.setOnEditorActionListener { _, keyCode, _ ->
            if (keyCode == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                binding.buttonChangePassword.performClick()
                return@setOnEditorActionListener true
            }
            false
        }

    }

    fun changeMyPassword(){
        val currentPassword= binding.textCurrentPassword.text.toString()
        val newPassword= binding.textNewPassword.text.toString()
        val confirmPassword= binding.textConfirmPassword.text.toString()
        viewModel.setLocalPassword(currentPassword,newPassword,  confirmPassword, requireContext())
    }
}