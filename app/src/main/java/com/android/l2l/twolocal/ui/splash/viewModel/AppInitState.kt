package com.android.l2l.twolocal.ui.splash.viewModel

sealed class AppInitState {
    object StartIntro : AppInitState()
//    object StartSecurity : AppInitState()
    object StartLogin : AppInitState()
    object StartMain : AppInitState()

}