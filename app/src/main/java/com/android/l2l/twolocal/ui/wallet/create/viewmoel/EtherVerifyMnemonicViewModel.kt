package com.android.l2l.twolocal.ui.wallet.create.viewmoel;

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class EtherVerifyMnemonicViewModel
@Inject constructor(
    private val etherRepository: CryptoCurrencyRepositoryHelper,
    private val context: Context
) : BaseViewModel() {


    private val _walletLiveData = MutableLiveData<ViewState<String>>()
    val walletLiveData: LiveData<ViewState<String>>
        get() = _walletLiveData

    private val _verifyMnemonicLiveData = MutableLiveData<ViewState<Boolean>>()
    val verifyMnemonicLiveData: LiveData<ViewState<Boolean>>
        get() = _verifyMnemonicLiveData

    fun loadMnemonic() {
        etherRepository.getMnemonic().withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .doOnError {
                _walletLiveData.value = ViewState.Error(GeneralError().withError(it))
            }
            .subscribe({ it ->
                _walletLiveData.value = ViewState.Success(it)
            }, { it.printStackTrace() })
    }

    fun verifyMnemonic(mnemonic: String ) {
        etherRepository.getMnemonic().withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .doOnError {
                _verifyMnemonicLiveData.value = ViewState.Error(GeneralError().withError(it))
            }
            .subscribe({ it ->
                if(it == mnemonic)
                    _verifyMnemonicLiveData.value = ViewState.Success(true)
                else
                    _verifyMnemonicLiveData.value = ViewState.Error(GeneralError().withError(message = context.getString(R.string.ether_verification_failed)))
            }, { it.printStackTrace() })
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}