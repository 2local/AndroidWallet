package com.android.l2l.twolocal.utils

import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WalletFactoryTest {

    @Test
    fun `check Default Ether Wallet Fields`() {
        val wallet = WalletFactory.getWallet(CryptoCurrencyType.ETHEREUM, "address", "uniquekey1")

        assertThat(wallet.walletName).isNotEmpty()
        assertThat(wallet.amount).isNotEmpty()
        assertThat(wallet.amount).isEqualTo("0")
        assertThat(wallet.type).isNotNull()
        assertThat(wallet.createWalletDate).isNotEqualTo(0)
        assertThat(wallet.showingOrder).isNotNull()
        assertThat(wallet.currency).isNotNull()
        assertThat(wallet.userVerifiedMnemonic).isNotNull()
        assertThat(wallet.address).isNotNull()
    }

    @Test
    fun `check Default 2LC Wallet Fields`() {
        val wallet = WalletFactory.getWallet(CryptoCurrencyType.TwoLC, "address", "uniquekey1")

        assertThat(wallet.walletName).isNotEmpty()
        assertThat(wallet.amount).isNotEmpty()
        assertThat(wallet.amount).isEqualTo("0")
        assertThat(wallet.type).isNotNull()
        assertThat(wallet.createWalletDate).isNotEqualTo(0)
        assertThat(wallet.showingOrder).isNotNull()
        assertThat(wallet.currency).isNotNull()
        assertThat(wallet.userVerifiedMnemonic).isNotNull()
        assertThat(wallet.address).isNotNull()
    }

    @Test
    fun `check Transaction Scan Url`() {
        val scanUrl = WalletFactory.getTransactionScanUrl(CryptoCurrencyType.TwoLC, "0xsdfsse4566ert")

        assertThat(scanUrl).isNotNull()
        assertThat(scanUrl).isNotEmpty()
    }
}