package com.george.weibo.logic.network

import com.george.weibo.WeiboApplication
import com.george.weibo.logic.entity.WeiboListParam
import com.george.weibo.logic.entity.WeiboListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeiboService {

    @GET("home_timeline.json?access_token=${WeiboApplication.TOKEN}")
    fun getWeiboList(@Query("page") page : Int, @Query("count") count : Int) : Call<WeiboListResponse>

}