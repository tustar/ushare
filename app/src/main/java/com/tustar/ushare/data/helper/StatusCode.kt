package com.tustar.ushare.data.helper

import com.google.gson.JsonParseException
import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication
import org.jetbrains.anko.toast
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException


object StatusCode {

    /**
     * -1 未知异常
     */
    private const val UNKNOWN_ERROR = -1

    /**
     * 200 响应成功
     */
    const val SUCCESS = 200

    /**
     * 400 请求参数缺失/格式错误/验签失败
     */
    const val PARAMS_ERROR = 400

    /**
     * 401 Token已失效
     */
    const val TOKEN_EXPIRED = 401

    /**
     * 405 请求方法不支持
     */
    const val METHOD_UNSUPPORT = 405

    /**
     * 415 内容类型不支持
     */
    const val CONTENT_UNSUPPORT = 415

    /**
     * 500 服务器内部错误
     */
    private const val SERVER_ERROR = 500

    /**
     * 1001 网络连接超时
     */
    const val NETWORK_ERROR = 1001

    /**
     * 1002 解析出错
     */
    private const val PARSE_ERROR = 1002

    /**
     * 1004 socket连接超时
     */
    private const val SOCKET_TIMEOUT_ERROR = 1004

    /**
     * 1005 socket连接不上
     */
    private const val CONNECT_ERROR = 1005

    /**
     * 1006 未知主机
     */
    private const val UNKNOWN_HOST_ERROR = 1006


    private fun exceptionToStatusCode(e: Throwable): Int {
        return when (e) {
            is HttpException -> e.code()
            // 网络
            is SocketTimeoutException ->
                SOCKET_TIMEOUT_ERROR

            is ConnectException ->
                CONNECT_ERROR

            is UnknownHostException ->
                UNKNOWN_HOST_ERROR

            // 解析
            is JsonParseException, is JSONException, is ParseException ->
                PARSE_ERROR

            // 未知错误
            else -> UNKNOWN_ERROR
        }
    }

    fun handleException(e: Throwable, block: ((Int) -> Int)? = null) {
        val code = exceptionToStatusCode(e)
        val resId = when (code) {
            SOCKET_TIMEOUT_ERROR -> R.string.socket_timeout_error
            CONNECT_ERROR -> R.string.connect_error
            UNKNOWN_HOST_ERROR -> R.string.unkown_host_error
            SERVER_ERROR, in (500..599) -> R.string.server_err
            else -> block?.invoke(code)
        }
        resId?.let {
            UShareApplication.context.toast(it)
        }
    }
}