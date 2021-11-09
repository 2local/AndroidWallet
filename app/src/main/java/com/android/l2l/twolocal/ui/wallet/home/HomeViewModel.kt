package com.android.l2l.twolocal.ui.wallet.home;

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.dataSourse.repository.crypto.CryptoCurrencyRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.wallet.WalletRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.TotalBalance
import com.android.l2l.twolocal.model.Wallet
import com.android.l2l.twolocal.model.WalletBalance
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.SingleLiveEvent
import com.android.l2l.twolocal.utils.WalletFactory
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class HomeViewModel
@Inject constructor(
    @Named("bscRepository")
    private val bscRepository: CryptoCurrencyRepositoryHelper,
    @Named("bnbRepository")
    private val bnbRepository: CryptoCurrencyRepositoryHelper,
    @Named("ethRepository")
    private val ethRepository: CryptoCurrencyRepositoryHelper,
    private val userSession: UserSessionHelper,
    private val walletRepository: WalletRepositoryHelper,
) : BaseViewModel() {


    private val _totalBalanceLiveData = MutableLiveData<ViewState<TotalBalance>>()
    val totalBalanceLiveData: LiveData<ViewState<TotalBalance>>
        get() = _totalBalanceLiveData

    private val _walletListLiveData = MutableLiveData<ViewState<List<Wallet>>>()
    val walletListLiveData: LiveData<ViewState<List<Wallet>>>
        get() = _walletListLiveData

    private val _incomeLiveData = SingleLiveEvent<ViewState<MutableList<Double>>>()
    val incomeLiveData: LiveData<ViewState<MutableList<Double>>>
        get() = _incomeLiveData

    private val _expenseLiveData = SingleLiveEvent<ViewState<MutableList<Double>>>()
    val expenseLiveData: LiveData<ViewState<MutableList<Double>>>
        get() = _expenseLiveData

    fun getAllWallets() {
        twoLCWalletTotalAmount()
        getListOfWallets()


        val ether: Single<WalletBalance> =
            ethRepository.getWalletBalance().onErrorReturnItem(WalletBalance("0", CryptoCurrencyType.ETHEREUM.name))
        val l2l: Single<WalletBalance> =
            bscRepository.getWalletBalance().onErrorReturnItem(WalletBalance("0", CryptoCurrencyType.TwoLC.name))
        val bnb: Single<WalletBalance> =
            bnbRepository.getWalletBalance().onErrorReturnItem(WalletBalance("0", CryptoCurrencyType.BINANCE.name))
        Single.zip(l2l, ether, bnb,
            {
                    l2lBalance: WalletBalance,
                    etherBalance: WalletBalance,
                    bnbBalance: WalletBalance,
                ->

                val etherWallet: Wallet? = walletRepository.getWallet(CryptoCurrencyType.ETHEREUM)
                if (etherWallet != null) {
                    etherWallet.amount = etherBalance.balance
                    walletRepository.saveWallet(etherWallet)
                }

                val l2lWallet: Wallet? = walletRepository.getWallet(CryptoCurrencyType.TwoLC)
                if (l2lWallet != null) {
                    l2lWallet.amount = l2lBalance.balance
                    walletRepository.saveWallet(l2lWallet)
                }

                val bnbWallet: Wallet? = walletRepository.getWallet(CryptoCurrencyType.BINANCE)
                if (bnbWallet != null) {
                    bnbWallet.amount = bnbBalance.balance
                    walletRepository.saveWallet(bnbWallet)
                }

            }).withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _totalBalanceLiveData.value = ViewState.Loading
            }
            .doOnError {
                _totalBalanceLiveData.value = ViewState.Error(GeneralError().withError(it))
            }
            .subscribe({
                twoLCWalletTotalAmount()
                getListOfWallets()
            }, { it.printStackTrace() })
    }

//    private fun getTotalBalanceOfAllWallets() {
//        walletRepository.getWalletList().withIO()
//            .doOnSubscribe {
//                addToDisposable(it)
//            }
//            .doOnError {
//                _totalBalanceLiveData.value = ViewState.Error(GeneralError().withError(it))
//            }
//            .subscribe({ walletList ->
//
//                onTotalWalletsAmount()
//
//            }, { it.printStackTrace() })
//    }

    private fun twoLCWalletTotalAmount() {
        val isShowAmount = userSession.getBalanceSeen()

        val twolcWallet: Wallet? = walletRepository.getWallet(CryptoCurrencyType.TwoLC)
        if (twolcWallet != null) {

            val totalPrice = twolcWallet.fiatPrice.toDouble()
            val twolc = twolcWallet.amount
            val totalBalance = TotalBalance(twolcWallet.type.symbol, twolc, totalPrice.toString(), isShowAmount)
            _totalBalanceLiveData.value = ViewState.Success(totalBalance)
        }
    }

    fun toggleBlindAmount() {
        walletRepository.getWalletList()
            .map { walletList ->
                val isShowAmount = userSession.balanceSeen
                userSession.balanceSeen = !isShowAmount
                walletList
            }.withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .doOnError {
                _totalBalanceLiveData.value = ViewState.Error(GeneralError().withError(it))
            }
            .subscribe({ walletList ->
                twoLCWalletTotalAmount()
                onWalletListLoaded(walletList as MutableList<Wallet>)
            }, { it.printStackTrace() })
    }

    private fun getListOfWallets() {
        walletRepository.getWalletList().withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .subscribe({ walletList ->
                onWalletListLoaded(walletList as MutableList<Wallet>)

            }, { it.printStackTrace() })
    }

    private fun onWalletListLoaded(walletList: MutableList<Wallet>) {
        val isShowAmount = userSession.getBalanceSeen()
        walletList.forEach { wallet ->
            wallet.showAmount = isShowAmount
        }
        val defaultWallets = WalletFactory.getAvailableWalletList()
        walletList.forEach { wl ->
            if (defaultWallets.contains(wl))
                defaultWallets.remove(wl)
        }

        if (defaultWallets.isNotEmpty()) walletList.add(Wallet(CryptoCurrencyType.NONE))
        _walletListLiveData.value = ViewState.Success(walletList)

    }

    fun getL2LTransactionsHistory() {
        bscRepository.getTransactionHistory().withIO()
            .doOnSubscribe {
                addToDisposable(it)
            }
            .doOnError {
            }
            .subscribe({ transactionList ->

                val incomeValues: MutableList<Double> = mutableListOf()
                val expenseValues: MutableList<Double> = mutableListOf()

                for (i in 0..11) {

                    var totalIncome = 0.0
                    var totalExpense = 0.0

                    transactionList.forEach {

                        val cal: Calendar = Calendar.getInstance()
                        cal.timeInMillis = it.timeStamp.toLong() * 1000
                        val time = cal.get(Calendar.MONTH)
                        if (i == time) {
                            if (it.isSend) {
                                totalExpense += it.normalValue.toDouble()
                            } else {
                                totalIncome += it.normalValue.toDouble()
                            }
                        }
                    }

                    incomeValues.add(totalIncome)
                    expenseValues.add(totalExpense)
                }
                _incomeLiveData.value = ViewState.Success(incomeValues)
                _expenseLiveData.value = ViewState.Success(expenseValues)
            }, { it.printStackTrace() })
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}