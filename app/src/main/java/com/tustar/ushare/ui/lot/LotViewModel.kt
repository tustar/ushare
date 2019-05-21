package com.tustar.ushare.ui.lot

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.tustar.ushare.R
import com.tustar.ushare.vmf.UserViewModelFactory
import com.tustar.ushare.base.BaseViewModel
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.data.entry.execute
import com.tustar.ushare.data.helper.Message
import com.tustar.ushare.data.helper.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.util.CommonDefine
import com.uber.autodispose.autoDisposable

class LotViewModel(private val repo: UserRepository) : BaseViewModel() {

    private var page = 1

    private val _users = MutableLiveData<MutableList<User>>()
    val users: LiveData<MutableList<User>>
        get() = _users

    fun getUsers() {
        repo.userList(page, CommonDefine.PAGE_SIZE)
                .autoDisposable(this)
                .subscribe({
                    it.execute(
                            ok = { users ->
                                _users.value = users
                            },
                            failure = { message ->
                                Message.handleFailure(message)
                            })
                }) {
                    StatusCode.handleException(it) {
                        R.string.login_captcha_get_err
                    }
                }
    }

    companion object {

        fun get(fragment: Fragment): LotViewModel {
            val application = checkApplication(checkActivity(fragment))
            return ViewModelProviders.of(fragment, UserViewModelFactory.getInstance(application))
                    .get(LotViewModel::class.java)
        }
    }
}