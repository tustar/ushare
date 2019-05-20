package com.tustar.ushare.base

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.clear()
    }


    companion object {
        fun checkApplication(activity: Activity): Application {
            return activity.application
                    ?: throw IllegalStateException("Your activity/fragment is not yet attached to "
                            + "Application. You can't request ViewModel before onCreate call.")
        }

        fun checkActivity(fragment: Fragment): Activity {
            return fragment.activity
                    ?: throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
        }
    }
}