package com.android.l2l.twolocal.ui.wallet.send

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.databinding.FragmentSendTokenSuccessBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.SendCryptoCurrency
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.PriceFormatUtils
import com.android.l2l.twolocal.utils.WalletFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SendTokenSuccessFragment : BaseFragment<BaseViewModel>(R.layout.fragment_send_token_success) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: BaseViewModel by viewModels { viewModelFactory }
    private val binding: FragmentSendTokenSuccessBinding by viewBinding(FragmentSendTokenSuccessBinding::bind)
    var sentCryptoCurrency: SendCryptoCurrency? = null

    companion object {
        const val SEND_CRYPTO_KEY = "SEND_CRYPTO_KEY"
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        handelBundle(arguments)
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent(), sentCryptoCurrency?.type).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handelBundle(arguments)
        setUpInfo(sentCryptoCurrency)

        binding.buttonDone.setOnClickListener {
            requireActivity().finish()
        }

        binding.btShare.setOnClickListener {
            sentCryptoCurrency?.let {
                val text = WalletFactory.getTransactionScanUrl(it.type, it.transactionHash)
                CommonUtils.shareText(requireContext(), text, getString(R.string.fragment_send_result_title))
            }
        }
    }

    private fun handelBundle(arguments: Bundle?) {
        arguments?.let {
            sentCryptoCurrency = it.getParcelable(SEND_CRYPTO_KEY)
        }
    }


    private fun setUpInfo(wallet: SendCryptoCurrency?) {
        wallet?.let {
            binding.textAmount.text = "${it.amount} ${it.type.symbol}"
            binding.txtFiatAmount.text = getString(R.string.s_s, it.fiatPriceFormat, it.currency.mySymbol)
            binding.textWalletNumber.text = it.address
            binding.txtTransactionHash.text = it.transactionHash
        }
    }
}