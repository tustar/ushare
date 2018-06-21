package com.tustar.ushare.ui.lot

import com.tustar.ushare.base.BasePresenter
import com.tustar.ushare.base.BaseView
import com.tustar.ushare.data.entry.User

interface LotContract {
    interface View : BaseView<Presenter> {
        fun showToast(resId: Int)
        fun updateUsers(users: MutableList<User>)
    }

    interface Presenter : BasePresenter {
        fun getUsers()
    }
}