package com.tustar.ushare.ui.mine

import com.tustar.ushare.base.BasePresenter
import com.tustar.ushare.base.BaseView
import com.tustar.ushare.data.entry.User

interface MineContract {
    interface View : BaseView<Presenter> {
        fun showToast(resId: Int)
        fun updateUserUI(user: User)
        fun toLoginUI()
    }

    interface Presenter : BasePresenter {
        fun onLogin()
        fun getUserInfo()
        fun updateNick(nick: String)
    }
}