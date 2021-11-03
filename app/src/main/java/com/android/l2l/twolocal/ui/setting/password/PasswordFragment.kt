package com.android.l2l.twolocal.ui.setting.password

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentPasswordBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.setting.di.DaggerSettingComponent
import com.android.l2l.twolocal.ui.setting.password.viewmodel.PasswordFormState
import com.android.l2l.twolocal.ui.setting.password.viewmodel.PasswordViewModel
import com.android.l2l.twolocal.utils.setEditTextError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class PasswordFragment : BaseFragment<PasswordViewModel>(R.layout.fragment_password) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: PasswordViewModel by viewModels { viewModelFactory }
    private val binding: FragmentPasswordBinding by viewBinding(FragmentPasswordBinding::bind)
    private lateinit var profileInfo: ProfileInfo


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerSettingComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.accountFormState.observe(viewLifecycleOwner, {
            when (it) {
                is PasswordFormState.PasswordError -> {
                    binding.textNewPassword.setEditTextError(it.error)
                }
                is PasswordFormState.RepeatPasswordError -> {
                    binding.textPasswordConfirmation.setEditTextError(it.error)
                }

                else -> {
                }
            }
        })


        viewModel.updateInfoLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    onSuccessDialog(getString(R.string.your_password_successfully_has_been_changed))
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error?.message)
                }
                else -> {
                }
            }
        })

        binding.buttonChangePassword.setOnClickListener {
            val password = binding.textNewPassword.text.toString().trim()
            val repeatPassword = binding.textPasswordConfirmation.text.toString().trim()
            viewModel.updatePassword(password, repeatPassword)
        }

        binding.toolbar.getBackBtn().setOnClickListener {
            findNavController().popBackStack()
        }

    }

}