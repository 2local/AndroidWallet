package com.android.l2l.twolocal.ui.wallet.send

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.model.SendCryptoCurrency
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SendTokenActivity : BaseActivity(R.layout.activity_send_token) {

    companion object {
        const val WALLET_TYPE_KEY = "WALLET_TYPE_KEY"
        const val WALLET_ADDRESS_KEY = "WALLET_ADDRESS_KEY"

        fun start(context: Context, walletType: CryptoCurrencyType, walletAddress: String? = null) {
            Intent(context, SendTokenActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        WALLET_TYPE_KEY to walletType,
                        WALLET_ADDRESS_KEY to walletAddress
                    )
                )
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        DaggerWalletComponent.factory().create(findAppComponent()).inject(this)
        super.onCreate(savedInstanceState)

        val bundle: Bundle? = intent.extras
        bundle?.let {

            val walletType = it.get(WALLET_TYPE_KEY) as CryptoCurrencyType
            val walletAddress = it.getString(WALLET_ADDRESS_KEY)

            val sentCryptoCurrency = SendCryptoCurrency()
            sentCryptoCurrency.type = walletType
            sentCryptoCurrency.address = walletAddress

            findNavController(R.id.nav_host_authentication)
                .setGraph(
                    R.navigation.nav_graph_send_token,
                    bundleOf(
                        SendTokenFragment.SEND_CRYPTO_KEY to sentCryptoCurrency,
                    )
                )
        }
    }

}