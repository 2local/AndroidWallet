package com.android.l2l.twolocal.ui.setting.profile

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentAccountInfoBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.ProfileInfo
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.setting.di.DaggerSettingComponent
import com.android.l2l.twolocal.ui.setting.profile.viewmodel.AccountInfoFormState
import com.android.l2l.twolocal.ui.setting.profile.viewmodel.AccountInfoViewModel
import com.android.l2l.twolocal.utils.setEditTextError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AccountInfoFragment : BaseFragment<AccountInfoViewModel>(R.layout.fragment_account_info) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: AccountInfoViewModel by viewModels { viewModelFactory }
    private val binding: FragmentAccountInfoBinding by viewBinding(FragmentAccountInfoBinding::bind)
    private lateinit var profileInfo: ProfileInfo


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerSettingComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.accountFormState.observe(viewLifecycleOwner, {
            when (it) {
                is AccountInfoFormState.UsernameError -> {
                    binding.editTextUsername.setEditTextError(it.error)
                }
                is AccountInfoFormState.PhoneError -> {
                    binding.textPhone.setEditTextError(it.error)
                }

                else -> {
                }
            }
        })

        viewModel.profileInfoLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    profileInfo = it.response
                    onUserDataLoaded(it.response)
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error)
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
                    onSuccessDialog(it.response.message)
//                    onSuccessDialog(getString(R.string.profile_info_updated))
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error)
                }
                else -> {
                }
            }
        })

        binding.buttonUpdateProfile.setOnClickListener {
            viewModel.updateProfile(getProfileInfo())
        }

        binding.toolbar.getBackBtn().setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getProfile()
    }

    private fun onUserDataLoaded(profileInfo: ProfileInfo) {
        binding.editTextUsername.setText(profileInfo.name)
        binding.textPhone.setText(profileInfo.mobile_number)
        binding.textEmail.setText(profileInfo.email)
        binding.editTextFirstName.setText(profileInfo.first_name)
        binding.editTextLastName.setText(profileInfo.last_name)
        binding.editTextBirthday.setText(profileInfo.birthday)
        binding.textCountry.setText(profileInfo.country)
        binding.textState.setText(profileInfo.state)
        binding.textCity.setText(profileInfo.city)
        binding.textAddress.setText(profileInfo.address)
        binding.textPostalCode.setText(profileInfo.post_code)
    }

    private fun getProfileInfo(): ProfileInfo {
        val profileUpdateInfo = ProfileInfo()
        profileUpdateInfo.name = binding.editTextUsername.text.toString()
        profileUpdateInfo.mobile_number = binding.textPhone.text.toString()
        profileUpdateInfo.email = binding.textEmail.text.toString()
        profileUpdateInfo.first_name = binding.editTextFirstName.text.toString()
        profileUpdateInfo.last_name = binding.editTextLastName.text.toString()
        profileUpdateInfo.birthday = binding.editTextBirthday.text.toString()
        profileUpdateInfo.country = binding.textCountry.text.toString()
        profileUpdateInfo.state = binding.textState.text.toString()
        profileUpdateInfo.city = binding.textCity.text.toString()
        profileUpdateInfo.address = binding.textAddress.text.toString()
        profileUpdateInfo.post_code = binding.textPostalCode.text.toString()
        profileUpdateInfo.user_Id = profileInfo.user_Id
        return profileUpdateInfo
    }
}