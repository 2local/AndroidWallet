package com.android.l2l.twolocal.ui.wallet.create

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.onErrorDialog
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentWalletBackupBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.ui.wallet.create.viewmoel.EtherBackupViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WalletBackupFragment : BaseFragment<EtherBackupViewModel>(R.layout.fragment_wallet_backup) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: EtherBackupViewModel by viewModels {viewModelFactory}
    private val binding: FragmentWalletBackupBinding by viewBinding(FragmentWalletBackupBinding::bind)
    lateinit var walletType: CryptoCurrencyType

    companion object {
        const val WALLET_TYPE_KEY = "WALLET_TYPE_KEY"
    }

    private fun handleBundle(arguments: Bundle?) {
        arguments?.let {
            walletType = it.get(WALLET_TYPE_KEY) as CryptoCurrencyType
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        handleBundle(arguments)
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent(), walletType).inject(this)

        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBundle(arguments)
        viewModel.createWallet()
        binding.txtCreateWallet.text = getString(R.string.create_wallet_wallet, walletType.symbol)
        viewModel.mnemonicLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    onMnemonicLoaded(it.response);
                }
                is ViewState.Error -> {
                    hideLoading()
                    onErrorDialog(it.error.message)
                }
            }
        })
        viewModel.createWalletLiveData.observe(requireActivity(), {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    viewModel.loadMnemonic()

                }
                is ViewState.Error -> {
                    hideLoading()
                    onErrorDialog(it.error.message)
                }
            }
        })


        binding.chkStored.setOnCheckedChangeListener { _, isChecked ->
                binding.btnGoNext.isEnabled = isChecked
        }

        binding.btnGoNext.setOnClickListener {
            val b = bundleOf(
                WalletVerifyMnemonic.WALLET_TYPE_KEY to walletType,
            )
           findNavController().navigate(R.id.action_etherBackupFragment_to_etherVerifyMnemonic, b)
        }

        binding.toolbar.getCloseBtn().setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun onMnemonicLoaded(mnemonic: String) {
       val words = mnemonic.replace(" ", WalletVerifyMnemonic.NMEMONIC_SPACE_SEPARATOR)
        binding.tvMnemonic.text = words
    }


}