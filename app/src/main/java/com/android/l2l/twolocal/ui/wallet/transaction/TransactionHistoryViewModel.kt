package com.android.l2l.twolocal.ui.wallet.transaction;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.WalletTransactionHistory
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class TransactionHistoryViewModel
@Inject constructor(
    private val walletRepository: WalletRepositoryHelper,
) : BaseViewModel() {


    private val _inComeLiveData = MutableLiveData<ViewState<List<String>>>()
    val inComeLiveData: LiveData<ViewState<List<String>>>
        get() = _inComeLiveData

    private val _transactionHistoryLiveData = MutableLiveData<ViewState<List<WalletTransactionHistory>>>()
    val transactionHistoryLiveData: LiveData<ViewState<List<WalletTransactionHistory>>>
        get() = _transactionHistoryLiveData

    fun getAllTransactionHistory() {
        walletRepository.getAllWalletTransactionList().withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ history ->
                _transactionHistoryLiveData.value = ViewState.Success(history)
            }, { it.printStackTrace() })
    }

    fun getSendTransactionHistory() {
        walletRepository.getAllWalletTransactionList()
            .map {
                it.filter { transaction->
                    transaction.isSend
                }
            }.withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ history ->

                _transactionHistoryLiveData.value = ViewState.Success(history)
            }, { it.printStackTrace() })
    }

    fun getReceiveTransactionHistory() {
        walletRepository.getAllWalletTransactionList()
            .map {
               it.filter { transaction->
                    !transaction.isSend
                }
            }
            .withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ history ->
                _transactionHistoryLiveData.value = ViewState.Success(history)
            }, { it.printStackTrace() })
    }

    fun getAllIncome() {
        walletRepository.getWalletTransactionList(CryptoCurrencyType.TwoLC).withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .doOnError {
            }
            .subscribe({ transactionList ->

                val incomeValues: MutableList<String> = mutableListOf()
//                val dates = CommonUtils.get_first_last_day_of_week()
                for (i in 1..7) {

                    var totalIncome = 0.0

                    transactionList.forEach {

                        val cal: Calendar = Calendar.getInstance()
                        cal.timeInMillis = it.timeStamp.toLong()*1000
                        val time = cal.get(Calendar.DAY_OF_WEEK)
                        if (i == time) {
                            if (!it.isSend) {
                                totalIncome += it.normalValue.toDouble()
                            }
                        }
                    }

                    incomeValues.add(totalIncome.toString())
                }
                _inComeLiveData.value = ViewState.Success(incomeValues)
            }, { it.printStackTrace() })
    }

    fun getAllExpense() {
        walletRepository.getWalletTransactionList(CryptoCurrencyType.TwoLC).withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .doOnError {
            }
            .subscribe({ transactionList ->

                val incomeValues: MutableList<String> = mutableListOf()
//                val dates = CommonUtils.get_first_last_day_of_week()
                for (i in 1..7) {

                    var totalIncome = 0.0

                    transactionList.forEach {

                        val cal: Calendar = Calendar.getInstance()
                        cal.timeInMillis = it.timeStamp.toLong()*1000
                        val time = cal.get(Calendar.DAY_OF_WEEK)
                        if (1 == time) {
                            if (it.isSend) {
                                totalIncome += it.normalValue.toDouble()
                            }
                        }
                    }

                    incomeValues.add(totalIncome.toString())
                }
                _inComeLiveData.value = ViewState.Success(incomeValues)
            }, { it.printStackTrace() })

    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}