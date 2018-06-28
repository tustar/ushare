package com.tustar.ushare.ui.login

import android.app.Application
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import com.tustar.ushare.base.BaseViewModel
import com.tustar.ushare.data.Injection
import com.tustar.ushare.util.Logger

class LoginViewModel(application: Application) : BaseViewModel(application) {

    init {
        val repo = Injection.provideUserRepository(application)
    }

    val mobile = ObservableField<String>()
    var mobileClear = ObservableInt()

    fun clearMobileText(v:View) {
        mobile.set("")
    }

    fun onMobileTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Logger.d("s = $s")
        val visible = if (s == null || s.isEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        mobileClear.set(visible)
    }
}