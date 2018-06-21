package com.tustar.ushare.ui.mine

import com.tustar.ushare.data.repository.UserRepository


class MinePresenter(private val view: MineContract.View,
                    private val repo: UserRepository) : MineContract.Presenter {

    init {
        view.presenter = this
    }
}