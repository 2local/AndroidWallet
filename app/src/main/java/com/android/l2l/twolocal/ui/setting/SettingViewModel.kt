package com.android.l2l.twolocal.ui.setting;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.common.withIO
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.repository.profile.ProfileRepositoryHelper
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError
import com.android.l2l.twolocal.dataSourse.utils.error.withError
import com.android.l2l.twolocal.model.enums.FiatType
import com.android.l2l.twolocal.ui.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class SettingViewModel
@Inject constructor(
    private val profileRepository: ProfileRepositoryHelper,
    private val userSession: UserSession,
) : BaseViewModel() {


    private val _updateInfoLiveData = MutableLiveData<ViewState<Boolean>>()
    val logoutLiveData: LiveData<ViewState<Boolean>>
        get() = _updateInfoLiveData

    private val _currencyTypeLiveData = MutableLiveData<ViewState<FiatType>>()
    val currencyTypeLiveData: LiveData<ViewState<FiatType>>
        get() = _currencyTypeLiveData

    fun resetWalletAndSignOut() {
        profileRepository.resetWalletAndSignOut().withIO()
            .doOnSubscribe {
                addToDisposable(it)
                _updateInfoLiveData.value = ViewState.Loading
            }
            .doOnSuccess { _updateInfoLiveData.value = ViewState.Success(it) }
            .doOnError { _updateInfoLiveData.value = ViewState.Error(GeneralError().withError(it)) }
            .subscribe({

            }, { })
    }

    fun isUserLoggedIn():Boolean = userSession.isUserLoggedIn //|| !userSession.apiToken.isNullOrEmpty()

    fun getCurrency() {
        val currencyType = userSession.currentCurrency
        _currencyTypeLiveData.value = ViewState.Success(currencyType)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}