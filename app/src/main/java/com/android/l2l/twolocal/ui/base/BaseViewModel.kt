package com.android.l2l.twolocal.ui.base;

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject


open class BaseViewModel @Inject constructor() : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun addToDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        Log.v("disposables", "disposables")
        super.onCleared()
    }
}
