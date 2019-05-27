package com.tustar.ushare.ui.mine

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication.Companion.context
import com.tustar.ushare.base.BaseViewModel
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.data.entry.execute
import com.tustar.ushare.data.helper.Message
import com.tustar.ushare.data.helper.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.vmf.UserViewModelFactory
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast


class MineViewModel(private val repo: UserRepository) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getUserInfo() {
        viewModelScope.launch {
            try {
                val response = repo.getUser().await()
                response.execute(
                        ok = { user ->
                            _user.value = user
                        },
                        failure = { message ->
                            Message.handleFailure(message)
                        })
            } catch (e: Exception) {
                StatusCode.handleException(e)
            }
        }
    }

    fun updateNick(nick: String) {
        viewModelScope.launch {
            try {
                val response = repo.updateNick(nick).await()
                response.execute(
                        ok = { user ->
                            context.toast(R.string.mine_nick_success)
                            _user.value = user
                        },
                        failure = { message ->
                            Message.handleFailure(message) {
                                context.toast(R.string.mine_nick_failure)
                            }
                        })
            } catch (e: Exception) {
                StatusCode.handleException(e) {
                    R.string.mine_nick_failure
                }
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