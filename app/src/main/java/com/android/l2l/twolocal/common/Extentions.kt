package com.android.l2l.twolocal.common

import android.app.Activity
import android.content.res.Resources
import com.android.l2l.twolocal.App
import com.android.l2l.twolocal.di.component.AppComponent

inline fun <T1: Any, T2: Any, R: Any> safeNullCheck(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


fun Activity.findAppComponent(): AppComponent {
    return (application as App).getAppComponent()
}