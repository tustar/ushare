package com.tustar.ushare.data.remote

import com.tustar.ushare.data.entry.Captcha
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.entry.Topic
import com.tustar.ushare.data.entry.User
import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface ApiService {

    // 发送验证码
    @FormUrlEncoded
    @POST("v1/user/captcha")
    fun captcha(@FieldMap params: Map<String, String>): Deferred<Response<Captcha, Any>>

    // 登录
    @FormUrlEncoded
    @POST("v1/user/login")
    fun login(@FieldMap params: Map<String, String>): Deferred<Response<User, Any>>

    // 更新权重值
    @FormUrlEncoded
    @POST("v1/user/weight")
    fun weight(@FieldMap params: Map<String, String>): Deferred<Response<User, Any>>

    // 更新权重值
    @FormUrlEncoded
    @POST("v1/user/nick")
    fun nick(@FieldMap params: Map<String, String>): Deferred<Response<User, Any>>

    // 获取用户列表
    @GET("/v1/user")
    fun userList(): Deferred<Response<MutableList<User>, Any>>

    // 获取文章列表
    @GET("/v1/topic")
    fun topicList(): Deferred<Response<MutableList<Topic>, Any>>

    // 更新权重值
    @GET("v1/user/info")
    fun info(@QueryMap params: Map<String, String>): Deferred<Response<User, Any>>
}