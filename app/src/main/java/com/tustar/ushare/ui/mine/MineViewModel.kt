package com.tustar.ushare.ui.mine

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication.Companion.context
import com.tustar.ushare.base.BaseViewModel
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.data.entry.execute
import com.tustar.ushare.data.helper.Message
import com.tustar.ushare.data.helper.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.vmf.UserViewModelFactory
import com.uber.autodispose.autoDisposable
import org.jetbrains.anko.toast


class MineViewModel(private val repo: UserRepository) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getUserInfo() {
        repo.getUser()
                .autoDisposable(this)
                .subscribe({
                    it.execute(
                            ok = { user ->
                                _user.value = user
                            },
                            failure = { message ->
                                Message.handleFailure(message)
                            })
                }) {
                    StatusCode.handleException(it)
                }
    }

    fun updateNick(nick: String) {
        repo.updateNick(nick)
                .autoDisposable(this)
                .subscribe({
                    it.execute(
                            ok = { user ->
                                context.toast(R.string.mine_nick_success)
                                _user.value = user
                            },
                            failure = { message ->
                                Message.handleFailure(message) {
                                    context.toast(R.string.mine_nick_failure)
                                }
                            })
                }) {
                    StatusCode.handleException(it) {
                        R.string.mine_nick_failure
                    }
                }

    }

    companion object {

        fun get(fragment: Fragment): MineViewModel {
            val application = checkApplication(checkActivity(fragment))
            return ViewModelProviders.of(fragment, UserViewModelFactory.getInstance(application))
                    .get(MineViewModel::class.java)
        }
    }
}