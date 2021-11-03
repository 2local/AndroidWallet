package com.android.l2l.twolocal.ui.wallet.send.viewmodel;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.R
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.contact.ContactRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.AddressBook
import com.android.l2l.twolocal.model.SendCryptoCurrency
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.WalletBalance
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.SingleLiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import java.math.BigDecimal
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class SendViewModel
@Inject constructor(
    private val walletRepository: WalletRepositoryHelper,
    private val etherRepository: CryptoCurrencyRepositoryHelper,
    private val contactRepository: ContactRepositoryHelper,
) : BaseViewModel() {


    private val _walletDetailLiveData = MutableLiveData<ViewState<Wallet>>()
    val walletDetailLiveData: LiveData<ViewState<Wallet>>
        get() = _walletDetailLiveData

    private val _sendTokenLiveData = SingleLiveEvent<ViewState<Boolean>>()
    val sendTokenLiveData: LiveData<ViewState<Boolean>>
        get() = _sendTokenLiveData

    private val _balanceLiveData = SingleLiveEvent<ViewState<Wallet>>()
    val balanceLiveData: LiveData<ViewState<Wallet>>
        get() = _balanceLiveData

    private val _contactListLiveData = MutableLiveData<ViewState<List<AddressBook>>>()
    val contactListLiveData: LiveData<ViewState<List<AddressBook>>>
        get() = _contactListLiveData

    private val _formState = SingleLiveEvent<SendFormState>()
    val formState: LiveData<SendFormState>
        get() = _formState

    fun getContactList(walletType: CryptoCurrencyType) {
        contactRepository.getContactList(walletType).withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ contacts ->
                _contactListLiveData.value = ViewState.Success(contacts)

            }, { it.printStackTrace() })
    }

    fun getWalletDetail(walletType: CryptoCurrencyType) {
        walletRepository.getWalletSingle(walletType).withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ wallet ->
                if(wallet!=null)
                    _walletDetailLiveData.value = ViewState.Success(wallet)

            }, { it.printStackTrace() })
    }

    fun getWalletBalance(walletType: CryptoCurrencyType) {
//        if (walletType == WalletType.TwoLC) {
//            l2lRepository.getWalletBalance()
//                .map { balance ->
//                    saveWallet(balance, walletType)
//                }.withIO()
//                .doOnSubscribe {
//                    addToDisposable(it)
//                    _balanceLiveData.value = ViewState.Loading
//                }
//                .subscribe({ wallet ->
//                    if(wallet!=null)
//                    _balanceLiveData.value = ViewState.Success(wallet)
//                }, { it.printStackTrace() })
//        } else if (walletType == WalletType.ETHEREUM) {
            etherRepository.getWalletBalance()
                .map { balance ->
                    saveWallet(balance, walletType)
                }.withIO()
                .doOnSubscribe {
                    addToDisposable(it)
                    _balanceLiveData.value = ViewState.Loading
                }
                .subscribe({ wallet ->
                    if(wallet!=null)
                    _balanceLiveData.value = ViewState.Success(wallet)
                }, { it.printStackTrace() })
//        }
    }

    private fun saveWallet(balance: WalletBalance, walletType: CryptoCurrencyType): Wallet? {
        val wallet = walletRepository.getWallet(walletType)
        if(wallet!=null) {
            wallet.amount = balance.balance
            walletRepository.saveWallet(wallet)
        }
        return wallet
    }

    fun sendEther(sentCryptoCurrency: SendCryptoCurrency) {
        val loginEnable = validDataChanged(sentCryptoCurrency.address, sentCryptoCurrency.amount, sentCryptoCurrency.type)
        if (loginEnable is SendFormState.IsDataValid) {
            if (loginEnable.isValid)
                _sendTokenLiveData.value = ViewState.Success(true)
        }
    }


    private fun validDataChanged(to: String, amount: String,  walletType: CryptoCurrencyType): SendFormState {
        var formState: SendFormState = SendFormState.IsDataValid(true)
        if (to.isBlank()) {
            formState = SendFormState.AddressError(R.string.error_empty_input)
            _formState.value = formState
        }else {
            if (walletType == CryptoCurrencyType.ETHEREUM) {
                if (!etherRepository.checkValidEtherAddress(to)) {
                    formState = SendFormState.AddressError(R.string.invalid_wallet_number)
                    _formState.value = formState
                }
            }
        }
        if (amount.isBlank()) {
            formState = SendFormState.AmountError(R.string.error_empty_input)
            _formState.value = formState
        }else{
            if (BigDecimal(amount).toDouble() == 0.0) {
                formState = SendFormState.AmountError(R.string.error_empty_input)
                _formState.value = formState
            }
        }


        return formState
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}