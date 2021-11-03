package com.android.l2l.twolocal.ui.setting.currency;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.model.enums.FiatType
import com.android.l2l.twolocal.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class CurrencyViewModel
@Inject constructor(
    private val userSession: UserSession,
) : BaseViewModel() {

    private val _updateCurrencyLiveData = MutableLiveData<ViewState<FiatType>>()
    val updateCurrencyLiveData: LiveData<ViewState<FiatType>>
        get() = _updateCurrencyLiveData


    fun saveCurrentCurrency(currencyType: FiatType) {
        userSession.saveCurrentCurrency(currencyType)
        _updateCurrencyLiveData.value = ViewState.Success(currencyType)
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}