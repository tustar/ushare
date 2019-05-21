package com.tustar.ushare.ktx

import android.content.Intent
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tustar.ushare.ui.login.LoginActivity
import java.util.concurrent.TimeUnit


fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run(action)
}

fun View.clicks() =
        RxView.clicks(this).throttleFirst(500L, TimeUnit.MILLISECONDS)!!

fun TextView.textChanges() =
        RxTextView.textChanges(this).throttleFirst(500L, TimeUnit.MILLISECONDS)!!

fun FragmentActivity.toLoginUI() {
    val intent = Intent(this, LoginActivity::class.java).apply {
    }
    startActivity(intent)
}

fun Fragment.toLoginUI() {
    activity?.toLoginUI()
}

fun FragmentActivity.setDarkStatusIcon(isDark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        val decorView = window.decorView
        if (decorView != null) {
            var visibility = decorView.systemUiVisibility
            visibility
            decorView.systemUiVisibility = if (isDark) {
                //设置黑色状态栏图标
                visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                //设置白色状态栏图标
                visibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    } else {
        try {
            val setDarkStatusIcon = Window::class.java.getDeclaredMethod("setDarkStatusIcon",
                    Boolean::class.javaPrimitiveType)
            setDarkStatusIcon.invoke(window, isDark)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

fun FragmentActivity.setStatusBarColor(colorId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = ContextCompat.getColor(this, colorId)
    }
}