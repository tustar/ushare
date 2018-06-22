package com.tustar.ushare.data.entry

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.content.Context
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Preference
import java.util.*

@Entity(tableName = "users")
data class User @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true)
        var id: Long,
        var mobile: String,
        @Ignore
        val token: String = "",
        var weight: Int = 0,
        var shared: Boolean = false,
        var nick: String = "大神",
        var type: String = "user",
        var last_at: Date = Date(),
        var next_at: Date = Date()
) {

    companion object {

        fun isLogin(context: Context): Boolean {
            var mobile by Preference(context, CommonDefine.PREF_KEY_USER_MOBILE, "")
            return mobile.isNotEmpty()

        }

        fun getNick(context: Context): String {
            var nick by Preference(context, CommonDefine.PREF_KEY_USER_NICK, "")
            return nick
        }
    }
}