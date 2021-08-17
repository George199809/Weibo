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

    val weiboList = ArrayList<Weibo>()

    val weiboListLiveData = Transformations.switchMap(paramLiveData){ param->
        Repository.getWeiboList(param)
    }

    fun getWeiboList(){
        LogUtils.d("WeiboViewModel", "into getWeiboList page:${paramLiveData.value?.page} & count:${paramLiveData.value?.count}")
        var newParam: WeiboListParam
        if(paramLiveData?.value == null){
            newParam = WeiboListParam(1,100)
        }else{
            newParam = WeiboListParam(paramLiveData.value!!.page+1,100)
            LogUtils.d("WeiboViewModel", "getWeiboList touch page:${newParam.page} & count:${newParam.count}")
        }
        LogUtils.d("WeiboViewModel", "weiboList size:${weiboList.size}")
        LogUtils.d("WeiboViewModel", "out getWeiboList page:${newParam.page} & count:${newParam.count}")

        paramLiveData.value = newParam
    }
}