package com.android.l2l.twolocal.ui.wallet.create.viewmoel;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.SingleLiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class EtherBackupViewModel
@Inject constructor(
    private val etherRepository: CryptoCurrencyRepositoryHelper,
) : BaseViewModel() {


    private val _walletLiveData = SingleLiveEvent<ViewState<String>>()
    val mnemonicLiveData: LiveData<ViewState<String>>
        get() = _walletLiveData

    private val _createMnemonicLiveData = SingleLiveEvent<ViewState<Boolean>>()
    val createWalletLiveData: LiveData<ViewState<Boolean>>
        get() = _createMnemonicLiveData

    fun createWallet(walletType: CryptoCurrencyType) {
        etherRepository.createEtherWallet().withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _createMnemonicLiveData.value = ViewState.Loading
            }.doOnError {
                _createMnemonicLiveData.value = ViewState.Error(GeneralError().withError(it))
            }.subscribe({ success ->
                if (success)
                    _createMnemonicLiveData.value = ViewState.Success(success)
                else
                    _createMnemonicLiveData.value = ViewState.Error(GeneralError().withError(messageRes = R.string.unknownError))

            }, { it.printStackTrace() })
    }

    fun loadMnemonic(walletType: CryptoCurrencyType) {
        etherRepository.getMnemonic().withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _walletLiveData.value = ViewState.Loading
            }
            .doOnError {
                _walletLiveData.value = ViewState.Error(GeneralError().withError(it))
            }
            .subscribe({ it ->
                _walletLiveData.value = ViewState.Success(it)
            }, { it.printStackTrace() })
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}