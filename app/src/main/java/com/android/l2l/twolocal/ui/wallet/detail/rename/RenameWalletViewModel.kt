package com.android.l2l.twolocal.ui.wallet.detail.rename;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.R
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
class RenameWalletViewModel
@Inject constructor(
    private val walletRepository: WalletRepositoryHelper,
) : BaseViewModel() {

    private val _validForm = MutableLiveData<RenameFormState>()
    val validForm: LiveData<RenameFormState>
        get() = _validForm

    private val _walletLiveData = MutableLiveData<ViewState<Wallet>>()
    val walletLiveData: LiveData<ViewState<Wallet>>
        get() = _walletLiveData

    private val _updateWalletLiveData = MutableLiveData<ViewState<Wallet>>()
    val updateWalletLiveData: LiveData<ViewState<Wallet>>
        get() = _updateWalletLiveData

    fun saveWallet(wallet: Wallet) {
        val loginEnable = validDataChanged(wallet.walletName)
        if (loginEnable is RenameFormState.IsDataValid) {
            if (loginEnable.isValid)
                doSaveWallet(wallet)
        }
    }

    private fun doSaveWallet(wallet: Wallet) {
        walletRepository.saveWalletSingle(wallet).withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ walletList ->
                _updateWalletLiveData.value = ViewState.Success(walletList)
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

    private fun validDataChanged(walletName: String): RenameFormState {
        var formState: RenameFormState = RenameFormState.IsDataValid(true)
        if (walletName.isBlank()) {
            formState = RenameFormState.NameError(R.string.error_empty_input)
            _validForm.value = formState
        }

        return formState
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}