package com.tustar.ushare.extension

import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.concurrent.TimeUnit

fun TextView.textChanges() =
        RxTextView.textChanges(this).throttleFirst(500L, TimeUnit.MILLISECONDS)!!