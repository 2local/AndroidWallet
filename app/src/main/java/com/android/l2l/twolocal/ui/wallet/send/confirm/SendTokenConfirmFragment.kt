package com.android.l2l.twolocal.ui.wallet.send.confirm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.FragmentSendTokenConfirmBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.SendCryptoCurrency
import com.android.l2l.twolocal.model.TransactionGas
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.enums.GasLimit
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.model.event.RefreshWalletEvent
import com.android.l2l.twolocal.ui.base.BaseFragment
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.ui.wallet.send.*
import com.android.l2l.twolocal.utils.CommonUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SendTokenConfirmFragment : BaseFragment<SendConfirmViewModel>(R.layout.fragment_send_token_confirm) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override val viewModel: SendConfirmViewModel by viewModels { viewModelFactory }
    private val binding: FragmentSendTokenConfirmBinding by viewBinding(FragmentSendTokenConfirmBinding::bind)


    private var sentCryptoCurrency: SendCryptoCurrency? = null
    private var gasAmount = "0"
    private var totalSendAmount = "0"
    private var gasLimit = GasLimit.NORMAL
    private var gasResponse: TransactionGas? = null

    companion object {
        const val SEND_CRYPTO_KEY = "SEND_CRYPTO_KEY"

        fun start(context: Context, wallet: Wallet) {
            Intent(context, SendTokenFragment::class.java).apply {
                val bundle = bundleOf(
                    SEND_CRYPTO_KEY to wallet,
                )
                putExtras(bundle)
                context.startActivity(this)
            }
        }
    }

    private fun handelBundle(arguments: Bundle?) {
        arguments?.let {
            sentCryptoCurrency = it.getParcelable(SEND_CRYPTO_KEY)
            sentCryptoCurrency?.let { wal ->
                totalSendAmount = wal.amount
            }
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        handelBundle(arguments)
        DaggerWalletComponent.factory().create(requireActivity().findAppComponent(), sentCryptoCurrency?.type).inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handelBundle(arguments)

        binding.buttonSend.setOnClickListener {
            sentCryptoCurrency?.let {
                viewModel.sendEther(it.address, it.amount, gasAmount)
            }
        }


        binding.toolbar.getBackBtn().setOnClickListener {
            findNavController().popBackStack()
        }


        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                if (checkedId == R.id.buttonHigh) {
                    gasLimit = GasLimit.HIGH
                    onNetworkFee()
                } else if (checkedId == R.id.buttonLow) {
                    gasLimit = GasLimit.LOW
                    onNetworkFee()
                } else if (checkedId == R.id.buttonNormal) {
                    gasLimit = GasLimit.NORMAL
                    onNetworkFee()
                }
            }
        }

        setUpLiveData()

        sentCryptoCurrency?.let {
            setUpInfo(BigDecimal(0), it.type)
            viewModel.getNetworkFee(it.type)
        }
    }

    private fun setUpInfo(gas: BigDecimal, walletType: CryptoCurrencyType) {
        sentCryptoCurrency?.let {
            // for token, gas pay from main network token
            if (walletType == it.type)
                it.amount = BigDecimal(totalSendAmount).minus(gas).toString()
            else it.amount = totalSendAmount
            binding.textWalletNumber.text = it.address
            binding.textAmount.text = getString(R.string.s_s, it.amountPriceFormat, it.type.symbol)
            binding.txtFiatAmount.text = getString(R.string.s_s, it.fiatPriceFormat, it.currency.mySymbol)
            checkValidAmount(it.amount)
        }
    }

    private fun checkValidAmount(amount: String) {
        if (BigDecimal(amount).compareTo(BigDecimal("0")) == -1) {
            binding.txtError.visibility = View.VISIBLE
            binding.txtError.text = getString(R.string.repository_send_amount_too_low)
            binding.buttonSend.isEnabled = false
        }
    }


    private fun setUpLiveData() {

        viewModel.networkFeeLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    gasResponse = it.response
                    onNetworkFee()
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error.message)
                }
            }
        })

        viewModel.sendTokenLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    sentCryptoCurrency?.transactionHash = it.response
                    EventBus.getDefault().post(RefreshWalletEvent())
                    findNavController().navigate(
                        R.id.action_sendTokenConfirmFragment_to_sendTokenConfirmationFragment,
                        bundleOf(
                            SendTokenSuccessFragment.SEND_CRYPTO_KEY to sentCryptoCurrency,
                        )
                    )
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error.message)
                }
            }
        })
    }

    private fun onNetworkFee() {
        gasResponse?.let { gasResponse ->
            if (gasLimit == GasLimit.NORMAL) {
                gasAmount = gasResponse.proposeGasPrice
                binding.networkFee.text = getString(
                    R.string.fragment_send_token_confirmation_network_fee,
                    CommonUtils.formatToDecimalPriceSixDigits(BigDecimal(gasResponse.proposeGasPrice)),
                    gasResponse.gasCryptoCurrencyType.symbol,
                    gasResponse.proposeGasFiatPrice
                )

                setUpInfo(BigDecimal(gasAmount), gasResponse.gasCryptoCurrencyType)
            }
            if (gasLimit == GasLimit.LOW) {
                gasAmount = gasResponse.safeGasPrice
                binding.networkFee.text = getString(
                    R.string.fragment_send_token_confirmation_network_fee,
                    CommonUtils.formatToDecimalPriceSixDigits(BigDecimal(gasResponse.safeGasPrice)),
                    gasResponse.gasCryptoCurrencyType.symbol,
                    gasResponse.safeGasFiatPrice
                )

                setUpInfo(BigDecimal(gasAmount), gasResponse.gasCryptoCurrencyType)
            }
            if (gasLimit == GasLimit.HIGH) {
                gasAmount = gasResponse.fastGasPrice
                binding.networkFee.text = getString(
                    R.string.fragment_send_token_confirmation_network_fee,
                    CommonUtils.formatToDecimalPriceSixDigits(BigDecimal(gasResponse.fastGasPrice)),
                    gasResponse.gasCryptoCurrencyType.symbol,
                    gasResponse.fastGasFiatPrice
                )

                setUpInfo(BigDecimal(gasAmount), gasResponse.gasCryptoCurrencyType)
            }
        }
    }

}