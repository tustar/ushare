package com.tustar.ushare.ui.main

import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication
import com.tustar.ushare.data.entry.execute
import com.tustar.ushare.data.helper.Message
import com.tustar.ushare.data.helper.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Logger
import com.tustar.ushare.util.Preference


class MainPresenter(private val view: MainContract.View,
                    private val repo: UserRepository) : MainContract.Presenter {

    init {
        view.presenter = this
    }


    override fun onLogin() {
        var token: String by Preference(UShareApplication.context,
                CommonDefine.HEAD_ACCESS_TOKEN, "")
        if (token.isNullOrEmpty()) {
            view.toLoginUI()
        }
    }

    override fun updateWeight(weight: Int) {
        Logger.i("weight = $weight")
        addSubscription(disposable = repo.updateWeight(weight).subscribe({
            it.execute(
                    ok = {
                        view.showToast(R.string.lot_weight_success)
                        view.updateLotUI()
                    },
                    failure = { message ->
                        Message.handleFailure(message) {
                            view.showToast(R.string.lot_weight_err)
                        }
                    }
            )
        }) {
            //
            StatusCode.handleException(it) {
                R.string.lot_weight_err
            }
        })
    }
}