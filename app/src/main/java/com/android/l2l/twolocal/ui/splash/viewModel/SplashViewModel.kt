package com.android.l2l.twolocal.ui.splash.viewModel;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.dataSourse.repository.exchangeRate.ExchangeRateRepositoryHelper
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.*
import com.android.l2l.twolocal.model.enums.TokenExchangeRatePairs
import com.android.l2l.twolocal.model.mapper.Mapper_ExchangeRate
import com.android.l2l.twolocal.model.response.ExchangeRateResponse
import com.android.l2l.twolocal.model.response.base.ApiSingleResponse
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.constants.AppConstants.*
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class SplashViewModel
@Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepositoryHelper,
    private val userSession: UserSessionHelper,
) : BaseViewModel() {

    private val _appInitLiveData = MutableLiveData<AppInitState>()
    val appInitLiveData: LiveData<AppInitState>
        get() = _appInitLiveData

    private val _cryptoPriceLiveData = MutableLiveData<ViewState<Boolean>>()
    val cryptoPriceLiveData: LiveData<ViewState<Boolean>>
        get() = _cryptoPriceLiveData


    fun initApp() {
        if (userSession.showIntro()) {
            userSession.setShowIntro(false)
            _appInitLiveData.value = AppInitState.StartIntro
        }else if (!userSession.isUserLoggedIn ) {
            _appInitLiveData.value = AppInitState.StartLogin
        } else {
            _appInitLiveData.value = AppInitState.StartMain
        }
    }

    fun getCryptoPrice() {
            Single.zip(
                exchangeRateRepository.getCoinExchangeRate(TokenExchangeRatePairs.TwoLC.pair)
                    .onErrorResumeNext { Single.just(ExchangeRateResponse(TokenExchangeRatePairs.TwoLC.pair, "0")) },
                exchangeRateRepository.getCoinExchangeRate(TokenExchangeRatePairs.BINANCE.pair)
                    .onErrorResumeNext { Single.just(ExchangeRateResponse(TokenExchangeRatePairs.BINANCE.pair, "0")) },
                exchangeRateRepository.getCoinExchangeRate(TokenExchangeRatePairs.ETHEREUM.pair)
                    .onErrorResumeNext { Single.just(ExchangeRateResponse(TokenExchangeRatePairs.ETHEREUM.pair, "0")) },
                {
                        towlc_usdt: ExchangeRateResponse,
                        bnb_usdt: ExchangeRateResponse,
                        eth_usdt: ExchangeRateResponse,
                    ->
                    val coinPrices: MutableList<CoinExchangeRate> = mutableListOf()
                    coinPrices.add(Mapper_ExchangeRate.mapperToCoinExchangeRate(bnb_usdt))
                    coinPrices.add(Mapper_ExchangeRate.mapperToCoinExchangeRate(towlc_usdt))
                    coinPrices.add(Mapper_ExchangeRate.mapperToCoinExchangeRate(eth_usdt))
                    exchangeRateRepository.saveCoinExchangeRate(coinPrices)

//                    profile.code == 200
                    true

                }).withIO()
                .doOnSubscribe {
                    addToDisposable(it)
                    _cryptoPriceLiveData.value = ViewState.Loading
                }
                .doOnError {
                    _cryptoPriceLiveData.value = ViewState.Error(GeneralError().withError(it))
                }
                .subscribe({
                    _cryptoPriceLiveData.value = ViewState.Success(it)
                }, { it.printStackTrace() })


    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}