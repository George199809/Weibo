package com.george.weibo.logic

import androidx.lifecycle.liveData
import com.george.weibo.logic.entity.Weibo
import com.george.weibo.logic.entity.WeiboListParam
import com.george.weibo.logic.network.WeiboNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

object Repository {
    fun getWeiboList(param : WeiboListParam) = liveData(Dispatchers.IO) {
        val result = try{
            val weiboListResponse = WeiboNetwork.getWeiboList(param)
            val weiboList = weiboListResponse.weiboList
            Result.success(weiboList)
        }catch (e : Exception){
            Result.failure<List<Weibo>>(e)
        }
        emit((result))
    }
}