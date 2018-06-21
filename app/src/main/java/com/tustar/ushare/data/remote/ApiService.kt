package com.tustar.ushare.data.remote

import com.tustar.ushare.data.entry.Code
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.entry.Topic
import com.tustar.ushare.data.entry.User
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    // 发送验证码
    @FormUrlEncoded
    @POST("v1/user/code")
    fun sendCode(@FieldMap params: Map<String, String>): Observable<Response<Code, Any>>

    // 登录
    @FormUrlEncoded
    @POST("v1/user/login")
    fun login(@FieldMap params: Map<String, String>): Observable<Response<User, Any>>

    // 更新权重值
    @FormUrlEncoded
    @POST("v1/user/weight")
    fun weight(@FieldMap params: Map<String, String>): Observable<Response<User, Any>>

    // 获取用户列表
    @GET("/v1/user")
    fun userList(): Observable<Response<MutableList<User>, Any>>

    // 获取文章列表
    @GET("/v1/topic")
    fun topicList(): Observable<Response<MutableList<Topic>, Any>>
}