package com.android.l2l.twolocal.utils

import android.util.Log
import com.android.l2l.twolocal.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

object RemoteConfigUtils {

    private const val TAG = "RemoteConfigUtils"

    //    private lateinit var remoteConfig: FirebaseRemoteConfig
    private const val MINIMUM_FETCH_INTERVAL_SECONDS = 6 * 3600L // 6 hours
    private const val MAINTENANCE_MSG = "maintenance_msg"
    private const val MAINTENANCE_MODE = "maintenance_mode"
    private const val ANNOUNCEMENT_MSG = "anouncement_msg"
    private const val SHOW_ANNOUNCEMENT_MSG = "show_anouncement"
    private const val ENABLE_INSTRUCTION_NO_WALLET_BALANCE_ZERO = "enable_instruction_when_no_wallet_added_and_balance_is_zero"
    private const val ENABLE_INSTRUCTION_NO_WALLET_BALANCE_ZERO_MESSAGE =
        "enable_instruction_when_no_wallet_added_and_balance_is_zero_msg" // html
    private val DEFAULTS: HashMap<String, Any> =
        hashMapOf(
            MAINTENANCE_MSG to "",
            MAINTENANCE_MODE to false,
            ANNOUNCEMENT_MSG to "",
            SHOW_ANNOUNCEMENT_MSG to false,
            ENABLE_INSTRUCTION_NO_WALLET_BALANCE_ZERO_MESSAGE to "",
            ENABLE_INSTRUCTION_NO_WALLET_BALANCE_ZERO to false
        )


    fun init() {
//        remoteConfig =
        getFirebaseRemoteConfig()
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {

        val remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 60 else MINIMUM_FETCH_INTERVAL_SECONDS

            fetchTimeoutInSeconds = 60
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(DEFAULTS)

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            Log.d(TAG, "addOnCompleteListener")
        }

        return remoteConfig
    }

    fun getMaintenanceMessage(remoteConfig: FirebaseRemoteConfig): String = remoteConfig.getString(MAINTENANCE_MSG)

    fun getMaintenanceMode(remoteConfig: FirebaseRemoteConfig): Boolean = remoteConfig.getBoolean(MAINTENANCE_MODE)

    fun getAnnouncementMessage(remoteConfig: FirebaseRemoteConfig): String = remoteConfig.getString(ANNOUNCEMENT_MSG)

    fun showAnnouncement(remoteConfig: FirebaseRemoteConfig): Boolean = remoteConfig.getBoolean(SHOW_ANNOUNCEMENT_MSG)

    fun isInstructionEnable(remoteConfig: FirebaseRemoteConfig): Boolean =
        remoteConfig.getBoolean(ENABLE_INSTRUCTION_NO_WALLET_BALANCE_ZERO)

    fun getInstructionMessage(remoteConfig: FirebaseRemoteConfig): String =
        remoteConfig.getString(ENABLE_INSTRUCTION_NO_WALLET_BALANCE_ZERO_MESSAGE)

}