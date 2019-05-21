package com.tustar.ushare.vmf

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tustar.ushare.data.Injection
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.ui.login.LoginViewModel
import com.tustar.ushare.ui.lot.LotViewModel
import com.tustar.ushare.ui.main.MainViewModel
import com.tustar.ushare.ui.mine.MineViewModel

class UserViewModelFactory(private val repo: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(LoginViewModel::class.java) ->
                        LoginViewModel(repo)
                    isAssignableFrom(LotViewModel::class.java) ->
                        LotViewModel(repo)
                    isAssignableFrom(MineViewModel::class.java) ->
                        MineViewModel(repo)
                    isAssignableFrom(MainViewModel::class.java) ->
                        MainViewModel(repo)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {
        @Volatile
        private var INSTANCE: UserViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE
                        ?: synchronized(UserViewModelFactory::class.java) {
                            INSTANCE
                                    ?: UserViewModelFactory(
                                            Injection.provideUserRepository(application.applicationContext))
                                            .also { INSTANCE = it }
                        }

    }
}