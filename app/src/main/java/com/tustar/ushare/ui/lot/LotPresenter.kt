package com.tustar.ushare.ui.lot

import com.tustar.ushare.R
import com.tustar.ushare.data.entry.execute
import com.tustar.ushare.data.helper.Message
import com.tustar.ushare.data.helper.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Logger

class LotPresenter(private val view: LotContract.View,
                   private val repo: UserRepository) : LotContract.Presenter {

    private var page = 1

    init {
        view.presenter = this
    }

    override fun getUsers() {
        addSubscription(disposable = repo.userList(page, CommonDefine.PAGE_SIZE)
                .subscribe({
                    it.execute(
                            ok = { users ->
                                view.updateUsers(users)
                            },
                            failure = { message ->
                                when (message) {
                                    Message.Unauthorized -> Logger.d("Sign Error")
                                    else -> {
                                    }
                                }
                            })
                }) {
                    StatusCode.handleException(it) {
                        R.string.login_captcha_get_err
                    }
                })
    }
}