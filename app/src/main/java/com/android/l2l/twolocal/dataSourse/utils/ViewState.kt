package com.android.l2l.twolocal.dataSourse.utils

import com.android.l2l.twolocal.dataSourse.utils.error.GeneralError

sealed class ViewState<out T> {

//    object Initial : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
    data class Success<T>(val response: T) : ViewState<T>()
    data class Error<T>(val error: GeneralError) : ViewState<T>()

    companion object {

    }

}