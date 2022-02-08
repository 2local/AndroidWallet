package com.android.l2l.twolocal.model;

import com.android.l2l.twolocal.coin.EthereumCoin
import com.android.l2l.twolocal.coin.TwoLocalCoin
import com.android.l2l.twolocal.model.enums.FiatType
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.utils.WalletFactory
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class WalletTest {



    @Test
    fun `check when basic wallet are same type must be equal`() {
        //GIVEN
        val wallet = Wallet(CryptoCurrencyType.ETHEREUM)
        val wallet2 = Wallet(CryptoCurrencyType.ETHEREUM)

        //THEN
        Truth.assertThat(wallet2).isEqualTo(wallet)

    }

    @Test
    fun `check when basic wallet are not same type must not be equal`() {
        //GIVEN
        val wallet3 = Wallet(CryptoCurrencyType.ETHEREUM)
        val wallet4 = Wallet(CryptoCurrencyType.TwoLC)

        //THEN
        Truth.assertThat(wallet3).isNotEqualTo(wallet4)
    }


    @Test
    fun `check wallet equality when wallets has different values, but has same type`() {
        //GIVEN
        val twoLocalCoinPrice = CoinExchangeRate(TwoLocalCoin.LATOEKN_EXCHANGE_RATE_PAIR_2LC_USDT,0.0032, 10.0)
        val ethereumCoinPrice = CoinExchangeRate(EthereumCoin.BITRUE_EXCHANGE_RATE_PAIR_ETH_USDT,0.0032, 10.0)
        val twoLocalCoin = TwoLocalCoin(FiatType.USD, twoLocalCoinPrice)
        val ethereumCoin = EthereumCoin(FiatType.EUR, ethereumCoinPrice)

        // GIVEN
        // wallet 1
        val walletTwoLocal = WalletFactory.getWallet(CryptoCurrencyType.TwoLC, "address1", "uniquekey1")
        walletTwoLocal.coin = twoLocalCoin
        walletTwoLocal.amount = "20"

        //wallet 2
        val walletEthereum = WalletFactory.getWallet(CryptoCurrencyType.ETHEREUM, "address2", "uniquekey2")
        walletEthereum.coin = ethereumCoin
        walletEthereum.amount = "22"

        //THEN
        Truth.assertThat(walletEthereum).isNotEqualTo(walletTwoLocal)
    }

}