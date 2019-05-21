package com.tustar.ushare.ktx

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.concurrent.TimeUnit


fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run(action)
}

fun View.clicks() =
        RxView.clicks(this).throttleFirst(500L, TimeUnit.MILLISECONDS)!!

fun TextView.textChanges() =
        RxTextView.textChanges(this).throttleFirst(500L, TimeUnit.MILLISECONDS)!!

