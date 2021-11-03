package com.android.l2l.twolocal.ui.wallet.detail.transactionDetail;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.WalletTransactionHistory
import com.android.l2l.twolocal.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class TransactionDetailViewModel
@Inject constructor(
    private val etherRepository: CryptoCurrencyRepositoryHelper,
) : BaseViewModel() {

    private val _transactionLiveData = MutableLiveData<ViewState<WalletTransactionHistory>>()
    val transactionLiveData: LiveData<ViewState<WalletTransactionHistory>>
        get() = _transactionLiveData


    fun getTransactionDetail(transactionHash: String) {
        etherRepository.getTransactionDetail(transactionHash).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _transactionLiveData.value = ViewState.Loading
            }
            .subscribe({
                _transactionLiveData.value = ViewState.Success(it)
            }, { it.printStackTrace() })
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}