package com.android.l2l.twolocal.ui.wallet.detail;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.WalletTransactionHistory
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.WalletBalance
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class WalletDetailViewModel
@Inject constructor(
    private val cryptoCurrencyRepository: CryptoCurrencyRepositoryHelper,
    private val walletRepository: WalletRepositoryHelper,
) : BaseViewModel() {


    private val _walletLiveData = MutableLiveData<ViewState<Wallet>>()
    val walletLiveData: LiveData<ViewState<Wallet>>
        get() = _walletLiveData

    private val _balanceLiveData = MutableLiveData<ViewState<Wallet>>()
    val balanceLiveData: LiveData<ViewState<Wallet>>
        get() = _balanceLiveData

    private val _transactionHistoryLiveData = MutableLiveData<ViewState<List<WalletTransactionHistory>>>()
    val transactionHistoryLiveData: LiveData<ViewState<List<WalletTransactionHistory>>>
        get() = _transactionHistoryLiveData

    fun getWalletDetail(walletType: CryptoCurrencyType) {
        walletRepository.getWalletSingle(walletType).withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ wallet ->
                if (wallet != null)
                    _walletLiveData.value = ViewState.Success(wallet)
            }, { it.printStackTrace() })
    }


    fun getWalletBalance(walletType: CryptoCurrencyType) {
            _balanceLiveData.value = ViewState.Loading
        cryptoCurrencyRepository.getWalletBalance()
                .map { balance ->
                    saveWallet(balance, walletType)
                }.withIO()
                .doOnSubscribe {
                    addToDisposable(it)
                }.doOnError {
                    _balanceLiveData.value = ViewState.Error(GeneralError().withError(it))
                }
                .subscribe({ wallet ->
                    if (wallet != null)
                        _balanceLiveData.value = ViewState.Success(wallet)
                }, { it.printStackTrace() })
    }

    private fun saveWallet(balance: WalletBalance, walletType: CryptoCurrencyType): Wallet? {
        val wallet = walletRepository.getWallet(walletType)
        if(wallet!=null) {
            wallet.amount = balance.balance
            walletRepository.saveWallet(wallet)
        }
        return wallet
    }

    ////////// ether history ////////////////////
    fun getTransactionHistory() {
        cryptoCurrencyRepository.getTransactionHistory().withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _transactionHistoryLiveData.value = ViewState.Loading
            }
            .subscribe({ history ->
                _transactionHistoryLiveData.value = ViewState.Success(history)
            }, { it.printStackTrace() })
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}