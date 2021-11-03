package com.android.l2l.twolocal.ui.wallet.detail.remove;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class RemoveWalletViewModel
@Inject constructor(
    private val walletRepository: WalletRepositoryHelper,
) : BaseViewModel() {

    private val _walletLiveData = MutableLiveData<ViewState<Wallet>>()
    val walletLiveData: LiveData<ViewState<Wallet>>
        get() = _walletLiveData

    private val _removeWalletLiveData = MutableLiveData<ViewState<Boolean>>()
    val removeWalletLiveData: LiveData<ViewState<Boolean>>
        get() = _removeWalletLiveData

    fun removeWallet(walletType: CryptoCurrencyType) {
        walletRepository.removeWallet(walletType).withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ success ->
                _removeWalletLiveData.value = ViewState.Success(success)
            }, { it.printStackTrace() })
    }

    fun getWalletDetail(walletType: CryptoCurrencyType) {
        walletRepository.getWalletSingle(walletType).withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ wallet ->
                if(wallet!=null)
                _walletLiveData.value = ViewState.Success(wallet)
            }, { it.printStackTrace() })
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}