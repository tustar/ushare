package com.tustar.ushare.ui.mine

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.tustar.ushare.Event
import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication
import com.tustar.ushare.UShareApplication.Companion.context
import com.tustar.ushare.ViewModelFactory
import com.tustar.ushare.base.BaseViewModel
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.data.entry.execute
import com.tustar.ushare.data.helper.Message
import com.tustar.ushare.data.helper.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Preference
import com.uber.autodispose.autoDisposable
import org.jetbrains.anko.toast


class MineViewModel(private val repo: UserRepository) : BaseViewModel() {

    private val _toLoginUIEvent = MutableLiveData<Event<Unit>>()
    val toLoginUIEvent: LiveData<Event<Unit>>
        get() = _toLoginUIEvent
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun onLogin() {
        var token: String by Preference(UShareApplication.context,
                CommonDefine.HEAD_ACCESS_TOKEN, "")
        if (token.isNullOrEmpty()) {
            _toLoginUIEvent.value = Event(Unit)
        } else {
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        repo.getUser()
                .autoDisposable(this)
                .subscribe({
                    it.execute(
                            ok = { user ->
                                context.toast(R.string.mine_user_success)
                                _user.value = user
                            },
                            failure = { message ->
                                Message.handleFailure(message) {
                                    context.toast(R.string.mine_user_failure)
                                }
                            })
                }) {
                    StatusCode.handleException(it) {
                        R.string.mine_user_failure
                    }
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
            return ViewModelProviders.of(fragment, ViewModelFactory.getInstance(application))
                    .get(MineViewModel::class.java)
        }
    }
}