package com.tustar.ushare.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.tustar.ushare.LiveEvent
import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication.Companion.context
import com.tustar.ushare.base.BaseViewModel
import com.tustar.ushare.data.entry.execute
import com.tustar.ushare.data.helper.Message
import com.tustar.ushare.data.helper.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.ui.mine.MineViewModel
import com.tustar.ushare.vmf.UserViewModelFactory
import com.uber.autodispose.autoDisposable
import org.jetbrains.anko.toast


class MainViewModel(private val repo: UserRepository) : BaseViewModel() {


    private val _updateLotUIEvent = MutableLiveData<LiveEvent<Unit>>()
    val updateLotUIEvent: LiveData<LiveEvent<Unit>>
        get() = _updateLotUIEvent

    fun updateWeight(weight: Int) {
        repo.updateWeight(weight)
                .autoDisposable(this)
                .subscribe({
                    it.execute(
                            ok = {
                                context.toast(R.string.lot_weight_success)
                                _updateLotUIEvent.value = LiveEvent(Unit)
                            },
                            failure = { message ->
                                Message.handleFailure(message) {
                                    context.toast(R.string.lot_weight_failure)
                                }
                            }
                    )
                }) {
                    //
                    StatusCode.handleException(it) {
                        R.string.lot_weight_failure
                    }
                }
    }

    companion object {

        fun get(activity: FragmentActivity): MainViewModel {
            val application = checkApplication(activity)
            return ViewModelProviders.of(activity, UserViewModelFactory.getInstance(application))
                    .get(MainViewModel::class.java)
        }
    }
}