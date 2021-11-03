package com.android.l2l.twolocal.ui.wallet.receive

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.binding.viewBinding
import com.android.l2l.twolocal.databinding.DialogReceiveShareBinding
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.utils.CommonUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DialogShareReceiveCurrency : DialogFragment(R.layout.dialog_receive_share) {


    private val binding: DialogReceiveShareBinding by viewBinding(DialogReceiveShareBinding::bind)
    var address: String? = null
    var amount: String? = null

    companion object {
        const val ADDRESS_KEY = "ADDRESS_KEY"
        const val CURRENCY_SYMBOL_KEY = "CURRENCY_SYMBOL_KEY"
        const val AMOUNT_KEY = "AMOUNT_KEY"
        const val FIAT_PRICE_KEY = "FIAT_PRICE_KEY"
        const val CURRENCY_KEY = "CURRENCY_KEY"

        fun newInstance(address: String, amount: String, coinSymbol: String, fiatPrice: String, currency: String) = DialogShareReceiveCurrency().apply {
            arguments = bundleOf(
                ADDRESS_KEY to address,
                CURRENCY_SYMBOL_KEY to coinSymbol,
                AMOUNT_KEY to amount,
                FIAT_PRICE_KEY to fiatPrice,
                CURRENCY_KEY to currency
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()

        arguments?.let {
            address = it.getString(ADDRESS_KEY)
            val currencySymbol = it.getString(CURRENCY_SYMBOL_KEY)
            amount = it.getString(AMOUNT_KEY)
            val fiat = it.getString(FIAT_PRICE_KEY)
            val currency = it.getString(CURRENCY_KEY)
            onWalletInfoLoaded(address, currencySymbol, amount, fiat, currency)
        }
    }

    private fun onWalletInfoLoaded(address: String?, coinSymbol: String?, amount: String?, fiat: String?, currency: String?) {
        binding.imageQr.setImageBitmap(CommonUtils.generateQrCode(address))
        binding.txtAddress.text = address
        binding.txtAmount.text = amount
        binding.txtCurrency.text = coinSymbol
        binding.txtFiat.text = "$fiat $currency"
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


    private fun setupClickListeners() {
        binding.btnShare.setOnClickListener {
            val shareBody = getString(R.string.receive_wallet_amount, address, amount)
            CommonUtils.shareText(requireContext(), shareBody, getString(R.string.receive_share_title))
        }
        binding.imageCross.setOnClickListener {
            dismiss()
        }
    }

}