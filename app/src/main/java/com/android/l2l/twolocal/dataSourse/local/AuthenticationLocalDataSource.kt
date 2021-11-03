package com.android.l2l.twolocal.dataSourse.local

import com.android.l2l.twolocal.dataSourse.local.prefs.UserSessionHelper
import com.android.l2l.twolocal.model.ProfileInfo
import javax.inject.Inject

class AuthenticationLocalDataSource @Inject constructor(private val appPreferences: UserSessionHelper) {

    fun saveProfileInfo(profile: ProfileInfo) = appPreferences.saveProfileInfo(profile)
    fun saveUserLoggedIn() = appPreferences.saveUserLoggedIn()
    fun saveApiToken(token: String) = appPreferences.saveApiToken(token)

}