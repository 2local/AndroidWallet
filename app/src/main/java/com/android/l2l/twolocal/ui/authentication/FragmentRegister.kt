package com.android.l2l.twolocal.ui.authentication


import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentRegisterBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.request.RegisterRequest
import com.android.l2l.twolocal.ui.authentication.di.DaggerAuthenticationComponent
import com.android.l2l.twolocal.ui.authentication.viewModel.formState.LoginFormState
import com.android.l2l.twolocal.ui.authentication.viewModel.RegisterViewModel
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.utils.setEditTextError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import com.android.l2l.twolocal.common.onMessageToast

@ExperimentalCoroutinesApi
class FragmentRegister : BaseFragment<RegisterViewModel>(R.layout.fragment_register) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: RegisterViewModel by viewModels { viewModelFactory }
    private val binding: FragmentRegisterBinding by viewBinding(FragmentRegisterBinding::bind)


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        DaggerAuthenticationComponent.factory().create(requireActivity().findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registerFormState.observe(viewLifecycleOwner, {
            when (it) {
                is LoginFormState.UsernameError -> {
                    binding.textUsername.setEditTextError(it.error)
                }
                is LoginFormState.EmailError -> {
                    binding.textEmail.setEditTextError(it.error)
                }
                is LoginFormState.PasswordError -> {
                    binding.textPassword.setEditTextError(it.error)
                }
                else -> {
                }
            }
        })


        viewModel.registerLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()

                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error?.message)
                }
                else -> {
                }
            }
        })

        binding.signUpSubmitButton.setOnClickListener {
            val username = binding.textUsername.text.toString()
            val email = binding.textEmail.text.toString()
            val password = binding.textPassword.text.toString()
            viewModel.registerUser(RegisterRequest(username, email, password))

        }
        binding.haveAnAccountLink.setOnClickListener {
//            val navOptions = NavOptions.Builder().setPopUpTo(R.id.register_dest, true).build()
            findNavController().navigate(R.id.action_register_to_login)
        }
    }
}