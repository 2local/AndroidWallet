package com.android.l2l.twolocal.ui.market;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.repository.market.MarketRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.MarketPlace
import com.android.l2l.twolocal.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class MarketListViewModel
@Inject constructor(
    private val marketRepository: MarketRepositoryHelper,
) : BaseViewModel() {


    private val _marketPlaceListLiveData = MutableLiveData<ViewState<List<MarketPlace>>>()
    val marketPlaceListLiveData: LiveData<ViewState<List<MarketPlace>>>
        get() = _marketPlaceListLiveData

    fun getMarketPlaces() {
        marketRepository.getMarketPlaces().withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _marketPlaceListLiveData.value = ViewState.Loading
            }
            .subscribe({
                _marketPlaceListLiveData.value = ViewState.Success(it.record.companies)
            }, {
                _marketPlaceListLiveData.value = ViewState.Error(GeneralError().withError(it))
                it.printStackTrace() })
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}