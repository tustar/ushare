package com.tustar.ushare.data.entry

object Message {

    /**
     * 无效签名
     */
    const val Unauthorized = "unauthorized"

    /**
     * 无效的短信验证码
     */
    const val INVALID_CAPTCHA = "invalid_captcha"
    /**
     * 插入失败
     */
    const val INSERT_FAILED = "insert_failed"
    /**
     * 无效token
     */
    const val INVALID_TOKEN = "invalid_token"
    /**
     * 过期token
     */
    const val EXPIRED_TOKEN = "expired_token"
}