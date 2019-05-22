package com.tustar.ushare.extension

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

fun View.clicks() =
        RxView.clicks(this).throttleFirst(500L, TimeUnit.MILLISECONDS)!!