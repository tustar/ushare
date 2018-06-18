package com.tustar.ushare.data.bean

import android.content.Context
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Preference

data class HttpResult<out D, out E>(
        val data: D,
        val message: String,
        val code: Int,
        val extra: E
) {
    companion object {
        const val OK = 200
        const val FAILURE = 500
    }
}

data class User(val id: Long,
                val mobile: String,
                val code: String,
                val token: String,
                val weight: Int,
                val shared: Boolean,
                val nick: String,
                val type: String,
                val last_at: Long,
                val next_at: Long) {

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

data class Topic(val id: Long,
                 val user_id: Long,
                 val title: String,
                 val description: String,
                 val created_at: Long,
                 val updated_at: Long,
                 val begin: Long,
                 val end: Long,
                 val shared: Boolean
)


data class Code(var captcha: String)
