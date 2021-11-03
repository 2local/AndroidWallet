package com.android.l2l.twolocal.ui.wallet.receive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.common.findAppComponent
import com.android.l2l.twolocal.common.hideKeyboard
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.databinding.ActivityReceiveBinding
import com.android.l2l.twolocal.di.viewModel.AppViewModelFactory
import com.android.l2l.twolocal.model.enums.InputType
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseActivity
import com.android.l2l.twolocal.ui.wallet.di.DaggerWalletComponent
import com.android.l2l.twolocal.utils.CommonUtils
import com.android.l2l.twolocal.utils.InputTextWatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ReceiveActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private val viewModel: ReceiveViewModel by viewModels { viewModelFactory }
    private val binding: ActivityReceiveBinding by viewBinding(ActivityReceiveBinding::inflate)

    private var inputType = InputType.CURRENCY
    private var walletType: CryptoCurrencyType? = null
    lateinit var wallet: Wallet
    lateinit var tempWallet: Wallet

    companion object {
        const val WALLET_TYPE_KEY = "WALLET_TYPE_KEY"

        fun start(context: Context, walletType: CryptoCurrencyType) {
            Intent(context, ReceiveActivity::class.java).apply {
                val bundle = bundleOf(
                    WALLET_TYPE_KEY to walletType,
                )
                putExtras(bundle)
                context.startActivity(this)
            }
        }
    }

    private fun handleBundle(bundle: Bundle?){
        walletType = bundle?.get(WALLET_TYPE_KEY) as CryptoCurrencyType
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleBundle(intent.extras)
//        bundle?.let {
//            walletType = it.get(WALLET_TYPE_KEY) as WalletType
//        }
        DaggerWalletComponent.factory().create(findAppComponent(),walletType).inject(this)

        if (inputType == InputType.CURRENCY)
            setEtherInputActive()
        else if (inputType == InputType.FIAT) {
            setUSDInputActive()
        }


        binding.toolbar.getBackBtn().setOnClickListener {
            finish()
        }

        binding.btnRequest.setOnClickListener {
            if (tempWallet.amount.isNotBlank())
                DialogShareReceiveCurrency.newInstance(
                    tempWallet.address,
                    tempWallet.amount,
                    tempWallet.type.symbol,
                    tempWallet.fiatPriceFormat,
                    tempWallet.currency.mySymbol
                ).show(supportFragmentManager, "")
        }

        binding.tvCopy.setOnClickListener {
            CommonUtils.copyToClipboard(this, wallet.address)
        }

        binding.edtEthAmount.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }

        binding.edtUsdAmount.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }

        binding.edtWalletNumber.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            false
        }

        binding.imgSwitch.setOnClickListener {
            when (inputType) {
                InputType.CURRENCY -> {
                    setUSDInputActive()
                    inputType = InputType.FIAT
                }
                InputType.FIAT -> {
                    setEtherInputActive()
                    inputType = InputType.CURRENCY
                }
            }
        }

        viewModel.walletDetailLiveData.observe(this, {
            when (it) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                    wallet = it.response
                    tempWallet = it.response
                    tempWallet.amount = "0.0"
                    onWalletInfoLoaded(wallet)
                }
                is ViewState.Error -> {
                    hideLoading()
                    onMessageToast(it.error?.message)
                }
            }
        })

        walletType?.let {
            viewModel.getWalletDetail(it)
        }
    }



    private fun onWalletInfoLoaded(wallet: Wallet) {
        binding.imgQr.setImageBitmap(CommonUtils.generateQrCode(wallet.address))
        binding.edtWalletNumber.setText(wallet.address)
        binding.txtWalletType.text = wallet.type.symbol
        binding.txtCurrency.text = wallet.currency.myName
        binding.txtNetwork.text = wallet.type.myNetwork
    }


    private fun setUSDInputActive() {
        binding.edtEthAmount.isEnabled = false
        binding.edtEthAmount.removeTextChangedListener(currencyWatcher)
        binding.edtUsdAmount.isEnabled = true
        binding.edtUsdAmount.addTextChangedListener(fiatWatcher)
    }

    private fun setEtherInputActive() {
        binding.edtEthAmount.isEnabled = true
        binding.edtEthAmount.addTextChangedListener(currencyWatcher)
        binding.edtUsdAmount.isEnabled = false
        binding.edtUsdAmount.removeTextChangedListener(fiatWatcher)
    }

    fun onPriceFormatted(wallet: Wallet) {
        when (inputType) {
            InputType.FIAT -> {
                binding.edtEthAmount.setText("")
                binding.edtEthAmount.hint = wallet.amount
            }
            InputType.CURRENCY -> {
                binding.edtUsdAmount.setText("")
                binding.edtUsdAmount.hint = wallet.fiatPrice
            }
        }
    }

    private val fiatWatcher: TextWatcher = object : InputTextWatcher() {
        override fun textChanged(amount: String) {
            tempWallet.fiatPrice = amount
            onPriceFormatted(tempWallet)
        }
    }

    private val currencyWatcher: TextWatcher = object : InputTextWatcher() {
        override fun textChanged(amount: String) {
            tempWallet.amount = amount
            onPriceFormatted(tempWallet)
        }
    }

}