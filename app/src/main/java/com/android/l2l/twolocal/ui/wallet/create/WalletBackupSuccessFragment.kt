package com.android.l2l.twolocal.ui.wallet.create

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentWalletBackupSuccessBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.event.RefreshWalletListEvent
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.wallet.detail.WalletDetailActivity
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.ui.wallet.create.viewmoel.EtherSuccessViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WalletBackupSuccessFragment : BaseFragment<EtherSuccessViewModel>(R.layout.fragment_wallet_backup_success) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: EtherSuccessViewModel by viewModels {viewModelFactory}
    private val binding: FragmentWalletBackupSuccessBinding by viewBinding(FragmentWalletBackupSuccessBinding::bind)

    lateinit var walletType: CryptoCurrencyType
    var message: String?= null

    companion object{
        const val MESSAGE_KEY = "MESSAGE_KEY"
        const val WALLET_TYPE_KEY = "WALLET_TYPE_KEY"

    }

    private fun handleBundle(arguments: Bundle?) {
        walletType = arguments?.get(WALLET_TYPE_KEY) as CryptoCurrencyType
        message = arguments?.getString(MESSAGE_KEY)
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        handleBundle(arguments)
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent(), walletType).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBundle(arguments)

        viewModel.activeWalletLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    EventBus.getDefault().post(RefreshWalletListEvent())
                    WalletDetailActivity.start(requireContext(), walletType)
                    requireActivity().finish()
                }
                is ViewState.Error -> {
                    hideLoading()
                    onErrorDialog(it.error.message)
                }
            }
        })

        message?.let {
            binding.message.text = message
        }

        binding.btnDone.setOnClickListener {
            viewModel.activeWallet(walletType)
        }

    }


}