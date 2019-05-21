package com.tustar.ushare.base

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

/**
 * base [ViewModel] that can automatically dispose itself in [onCleared].
 */
open class BaseViewModel : ViewModel(), LifecycleScopeProvider<BaseViewModel.ViewModelEvent> {

    // Subject backing the auto disposing of subscriptions.
    private val lifecycleEvents = BehaviorSubject.createDefault(
            ViewModelEvent.CREATED)

    /**
     * The events that represent the lifecycle of a [ViewModel].
     *
     * The [ViewModel] lifecycle is very simple. It is created
     * and then allows you to clean up any resources in the
     * [ViewModel.onCleared] method before it is destroyed.
     */
    enum class ViewModelEvent {
        CREATED, CLEARED
    }

    /**
     * The observable representing the lifecycle of the [ViewModel].
     *
     * @return [Observable] modelling the [ViewModel] lifecycle.
     */
    override fun lifecycle(): Observable<ViewModelEvent> {
        return lifecycleEvents.hide()
    }

    /**
     * Returns a [CorrespondingEventsFunction] that maps the
     * current event -> target disposal event.
     *
     * @return function mapping the current event to terminal event.
     */
    override fun correspondingEvents(): CorrespondingEventsFunction<ViewModelEvent> {
        return CORRESPONDING_EVENTS
    }

    override fun peekLifecycle(): ViewModelEvent? {
        return lifecycleEvents.value
    }

    /**
     * Emit the [ViewModelEvent.CLEARED] event to
     * dispose off any subscriptions in the ViewModel.
     */
    override fun onCleared() {
        lifecycleEvents.onNext(ViewModelEvent.CLEARED)
        super.onCleared()
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

        /**
         * Function of current event -> target disposal event. ViewModel has a very simple lifecycle.
         * It is created and then later on cleared. So we only have two events and all subscriptions
         * will only be disposed at [ViewModelEvent.CLEARED].
         */
        private val CORRESPONDING_EVENTS = CorrespondingEventsFunction<ViewModelEvent> { event ->
            when (event) {
                ViewModelEvent.CREATED -> ViewModelEvent.CLEARED
                else -> throw LifecycleEndedException(
                        "Cannot bind to ViewModel lifecycle after onCleared.")
            }
        }
    }
}