package com.android.l2l.twolocal.ui.wallet.create.viewmoel;

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
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.WalletFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class CreateWalletViewModel
@Inject constructor(
    private val walletRepositoryHelper: WalletRepositoryHelper,
) : BaseViewModel() {


    private val _walletListLiveData = MutableLiveData<ViewState<List<Wallet>>>()
    val walletListLiveData: LiveData<ViewState<List<Wallet>>>
        get() = _walletListLiveData



    fun getAvailableWalletList() {
        walletRepositoryHelper.getWalletList().withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }.doOnError {
                _walletListLiveData.value = ViewState.Error(GeneralError().withError(it))
            }.subscribe({ walletList ->
                val defaultWallets = WalletFactory.getAvailableWalletList()
                walletList.forEach { wl ->
                    if (defaultWallets.contains(wl))
                        defaultWallets.remove(wl)
                }
                _walletListLiveData.value = ViewState.Success(defaultWallets)

            }, { it.printStackTrace() })
    }



    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}