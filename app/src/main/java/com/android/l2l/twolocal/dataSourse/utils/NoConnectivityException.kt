package com.android.l2l.twolocal.dataSourse.utils

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = "Network Connection exceptionnnn"
}