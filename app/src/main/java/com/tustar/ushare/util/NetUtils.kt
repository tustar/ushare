package com.tustar.ushare.util

import com.tustar.ushare.BuildConfig
import com.tustar.ushare.UShareApplication

object NetUtils {

    private const val PARTNER = "com.tustar.ushare"
    private const val UTF_8 = "utf-8"
    const val SIGN = "sign"

    private fun getBasicParams(): MutableMap<String, String> {
        val params = mutableMapOf<String, String>()
        params["partner"] = PARTNER
        val timestamp = (System.currentTimeMillis() / 1000).toString()
        params["timestamp"] = timestamp
        return params
    }

    fun getSignedParams(params: MutableMap<String, String> = mutableMapOf()):
            MutableMap<String, String> {
        params.putAll(getBasicParams())
        val signStr = RsaUtils.buildSignContent(params)
        val privateKey = RsaUtils.getPrivateKey(UShareApplication.context, BuildConfig.PRIVATE_KEY)
        val sign = RsaUtils.rsaSign(signStr, privateKey, UTF_8)
        params[SIGN] = sign ?: ""
        return params
    }

}