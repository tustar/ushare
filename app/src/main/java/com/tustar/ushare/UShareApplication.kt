package com.tustar.ushare

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

class UShareApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        var context: Context by Delegates.notNull()
    }
}