package com.android.l2l.twolocal.ui.wallet.create.viewmoel;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
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
class EtherSuccessViewModel
@Inject constructor(
    private val walletRepository: WalletRepositoryHelper,
) : BaseViewModel() {


    private val _activeWalletLiveData = MutableLiveData<ViewState<Boolean>>()
    val activeWalletLiveData: LiveData<ViewState<Boolean>>
        get() = _activeWalletLiveData


    fun activeWallet(walletType: CryptoCurrencyType) {
        walletRepository.activeWallet(walletType).withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .doOnError {
                _activeWalletLiveData.value = ViewState.Error(GeneralError().withError(it))
            }
            .subscribe({
                _activeWalletLiveData.value = ViewState.Success(true)
            }, { it.printStackTrace() })
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}