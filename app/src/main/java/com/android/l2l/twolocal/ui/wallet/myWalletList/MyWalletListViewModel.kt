package com.android.l2l.twolocal.ui.wallet.myWalletList;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.WalletFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class MyWalletListViewModel
@Inject constructor(
    private val walletRepository: WalletRepositoryHelper,
) : BaseViewModel() {


    private val _walletListLiveData = MutableLiveData<ViewState<List<Wallet>>>()
    val walletListLiveData: LiveData<ViewState<List<Wallet>>>
        get() = _walletListLiveData

    private val _canCreateWalletLiveData = MutableLiveData<ViewState<Boolean>>()
    val canCreateWalletLiveData: LiveData<ViewState<Boolean>>
        get() = _canCreateWalletLiveData

    fun getMyWalletList() {
        walletRepository.getWalletList().withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ walletList ->
                _walletListLiveData.value = ViewState.Success(walletList)
                canAddNewWallet(walletList)
            }, { it.printStackTrace() })
    }

    fun canAddNewWallet(walletList: List<Wallet>) {
        val defaultWallets = WalletFactory.getAvailableWalletList()
        walletList.forEach { wl ->
            if (defaultWallets.contains(wl))
                defaultWallets.remove(wl)
        }
        _canCreateWalletLiveData.value = ViewState.Success(defaultWallets.size!= 0)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}