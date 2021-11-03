package com.android.l2l.twolocal.ui.wallet.create.viewmoel;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class EtherRestoreViewModel
@Inject constructor(
    private val coinRepository: CryptoCurrencyRepositoryHelper,
) : BaseViewModel() {


    private val _restoreWalletLiveData = MutableLiveData<ViewState<Boolean>>()
    val restoreWalletLiveData: LiveData<ViewState<Boolean>>
        get() = _restoreWalletLiveData



    fun restoreWalletFromMnemonic(mnemonic: String) {
        coinRepository.restoreWalletFromMnemonic(mnemonic).withIO()
                .doOnSubscribe {
                    addToDisposable(it)
                    _restoreWalletLiveData.value = ViewState.Loading
                }
                .doOnError {
                    _restoreWalletLiveData.value = ViewState.Error(GeneralError().withError(it))
                }
                .subscribe({ it ->
                    _restoreWalletLiveData.value = ViewState.Success(true)
                }, { it.printStackTrace() })
    }

    fun restoreWalletFromPrivateKey(privateKey: String) {
        coinRepository.restoreWalletFromPrivateKey(privateKey).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _restoreWalletLiveData.value = ViewState.Loading
            }
            .doOnError {
                _restoreWalletLiveData.value = ViewState.Error(GeneralError().withError(it))
            }
            .subscribe({ it ->
                _restoreWalletLiveData.value = ViewState.Success(true)
            }, { it.printStackTrace() })
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}