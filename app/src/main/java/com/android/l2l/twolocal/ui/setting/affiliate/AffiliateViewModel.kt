package com.android.l2l.twolocal.ui.setting.affiliate;

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.l2l.twolocal.dataSourse.local.prefs.UserSession
import com.android.l2l.twolocal.dataSourse.utils.ViewState
import com.android.l2l.twolocal.ui.base.BaseViewModel
import com.android.l2l.twolocal.utils.constants.AppConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class AffiliateViewModel
@Inject constructor(
    private val userSession: UserSession,
) : BaseViewModel() {

    private val _preferLinkLiveData = MutableLiveData<ViewState<String>>()
    val preferLinkLiveData: LiveData<ViewState<String>>
        get() = _preferLinkLiveData


    fun getMyPreferLink() {
        if (userSession.profileInfo!=null) {
            val userID = userSession.profileInfo.affiliateCode
            _preferLinkLiveData.value = ViewState.Success(java.lang.String.format(AppConstants.REFERRAL_LINK_BASE, userID))
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}