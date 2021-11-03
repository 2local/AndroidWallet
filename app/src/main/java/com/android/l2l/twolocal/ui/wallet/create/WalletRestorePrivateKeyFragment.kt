package com.android.l2l.twolocal.ui.wallet.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentWalletPrivateKeyRestoreBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.scanner.ScanActivity
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.ui.wallet.create.viewmoel.EtherRestoreViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WalletRestorePrivateKeyFragment : BaseFragment<EtherRestoreViewModel>(R.layout.fragment_wallet_private_key_restore) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: EtherRestoreViewModel by viewModels {viewModelFactory}
    private val binding: FragmentWalletPrivateKeyRestoreBinding by viewBinding(FragmentWalletPrivateKeyRestoreBinding::bind)
    lateinit var walletType: CryptoCurrencyType

    companion object {
        const val WALLET_TYPE_KEY = "WALLET_TYPE_KEY"
    }

    private fun handleBundle(arguments: Bundle?) {
        walletType = arguments?.get(WALLET_TYPE_KEY) as CryptoCurrencyType
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        handleBundle(arguments)
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent(), walletType).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.restoreWalletLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    onWalletRestored()
                }
                is ViewState.Error -> {
                    hideLoading()
                    onErrorDialog(it.error.message)
                }
            }
        })

        binding.btnSubmit.setOnClickListener {
            if (binding.etPrivateKey.text?.isNotBlank() == true)
                viewModel.restoreWalletFromPrivateKey(binding.etPrivateKey.text.toString())
        }
        binding.tvClear.setOnClickListener {
            binding.etPrivateKey.text?.clear()
        }

        binding.imageScan.setOnClickListener {
            val intent = Intent(context, ScanActivity::class.java)
            resultLauncher.launch(intent)
        }

        binding.toolbar.getCloseBtn().setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            binding.etPrivateKey.setText(data!!.extras!!.getString(ScanActivity.KEY_BUNDLE_RESULT))
        }
    }

    private fun onWalletRestored() {
        val bundle = bundleOf(
            WalletBackupSuccessFragment.MESSAGE_KEY to getString(R.string.restore_eth_success, walletType.symbol),
            WalletBackupSuccessFragment.WALLET_TYPE_KEY to walletType,
        )
        findNavController().navigate(R.id.action_walletRestorePrivateKeyFragment_to_etherBackupSuccessFragment, bundle)
    }

}