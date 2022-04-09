package com.android.l2l.twolocal.ui.wallet.send.confirm;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.TransactionGas
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.PriceFormatUtils
import com.android.l2l.twolocal.utils.SingleLiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class SendConfirmViewModel
@Inject constructor(
    private val cryptoRepository: CryptoCurrencyRepositoryHelper,
) : BaseViewModel() {


    private val _sendTokenLiveData = SingleLiveEvent<ViewState<String>>()
    val sendTokenLiveData: LiveData<ViewState<String>>
        get() = _sendTokenLiveData


    private val _networkFeeLiveData = SingleLiveEvent<ViewState<TransactionGas>>()
    val networkFeeLiveData: LiveData<ViewState<TransactionGas>>
        get() = _networkFeeLiveData


    fun sendEther(to: String, amount: String, gasLimit: String) {
        cryptoRepository.sendAmount(to,  PriceFormatUtils.stringToBigDecimal(amount), PriceFormatUtils.stringToBigDecimal(gasLimit)).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _sendTokenLiveData.value = ViewState.Loading
            }
            .doOnError {
                _sendTokenLiveData.value = ViewState.Error(GeneralError().withError(it))
            }
            .subscribe({ transactionHash ->
                _sendTokenLiveData.value = ViewState.Success(transactionHash)

            }, { it.printStackTrace() })
    }

    fun getNetworkFee() {
        cryptoRepository.getNetworkFee().withIO()
                .doOnSubscribe {
                    addToDisposable(it)
                    _networkFeeLiveData.value = ViewState.Loading
                }
                .doOnError {
                    _networkFeeLiveData.value = ViewState.Error(GeneralError().withError(it))
                }
                .subscribe({ wallet ->
                    _networkFeeLiveData.value = ViewState.Success(wallet)

                }, { it.printStackTrace() })
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}