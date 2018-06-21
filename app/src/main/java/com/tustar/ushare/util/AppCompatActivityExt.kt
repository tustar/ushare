package com.tustar.ushare.util

import android.support.annotation.IdRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run(action)
}