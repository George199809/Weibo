package com.george.weibo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.george.weibo.logic.Repository
import com.george.weibo.logic.entity.Weibo
import com.george.weibo.logic.entity.WeiboListParam
import com.george.weibo.tools.LogUtils

class WeiboViewModel : ViewModel(){
    private val paramLiveData = MutableLiveData<WeiboListParam>()
    private val userIdLiveData = MutableLiveData<String>()

    val weiboList = ArrayList<Weibo>()

    val weiboListLiveData = Transformations.switchMap(paramLiveData){ param->
        Repository.getWeiboList(param)
    }

    fun getWeiboList(){
        LogUtils.d("WeiboViewModel", "into getWeiboList page:${paramLiveData.value?.page} & count:${paramLiveData.value?.count}")
        var newParam: WeiboListParam = WeiboListParam(1,20)
        paramLiveData.value?.apply {
            newParam = if (count <= 80) WeiboListParam(page, count + 20)
            else WeiboListParam(page + 1, 20)
        }
        paramLiveData.value = newParam
    }

    fun refreshWeiboList(){
        paramLiveData.value = WeiboListParam(1,20)
    }

    val userInfo  = Transformations.switchMap(userIdLiveData){  uid->
        Repository.getUerInfo(uid)
    }

    fun getUser(uid : String){
        userIdLiveData.value = uid
    }
}