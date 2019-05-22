package com.tustar.ushare.extension

import android.content.Intent
import android.os.Build
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tustar.ushare.ui.login.LoginActivity

fun FragmentActivity.toLoginUI() {
    val intent = Intent(this, LoginActivity::class.java).apply {
    }
    startActivity(intent)
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