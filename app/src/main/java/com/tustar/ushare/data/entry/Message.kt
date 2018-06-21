package com.tustar.ushare.data.entry

object Message {

    /**
     * 无效签名
     */
    const val Unauthorized = "unauthorized"

    /**
     * 无效的短信验证码
     */
    const val INVALID_MSG_CODE = "invalid_msg_code"
    /**
     * 插入失败
     */
    const val INSERT_FAIL = "insert_fail"
    /**
     * 无效token
     */
    const val INVALID_TOKEN = "invalid_token"
    /**
     * 过期token
     */
    const val EXPIRED_TOKEN = "expired_token"
}