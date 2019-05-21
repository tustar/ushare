package com.tustar.ushare

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tustar.ushare.data.Injection
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.ui.login.LoginViewModel
import com.tustar.ushare.ui.lot.LotViewModel

class ViewModelFactory(private val repo: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(LoginViewModel::class.java) ->
                        LoginViewModel(repo)
                    isAssignableFrom(LotViewModel::class.java)-> {
                        LotViewModel(repo)
                    }
                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE
                        ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE
                            ?: ViewModelFactory(
                                    Injection.provideUserRepository(application.applicationContext))
                            .also { INSTANCE = it }
                }

    }
}