package com.android.l2l.twolocal.sdk.binancesmartchainsdk.util;

import com.android.l2l.twolocal.dataSourse.repository.crypto.utils.CryptoWalletUtils
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4::class)
class CryptoWalletUtilsTest {

    @Before
    fun setUp() {
    }

    @Test
    fun checkValidPrivateKeyFromMnemonic() {
      val privateKey =  CryptoWalletUtils.getPrivateKeyFromMnemonic("energy endless attract kangaroo advice helmet moon brown potato expect region tragic")
        Truth.assertThat(privateKey).isEqualTo("552be456cb5096f5bd9c99d2d24545a615b620b810ed5fbc9053760a87bd04a6")
    }

}