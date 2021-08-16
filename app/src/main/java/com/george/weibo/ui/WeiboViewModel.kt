package com.george.weibo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.george.weibo.logic.Repository
import com.george.weibo.logic.entity.Weibo
import com.george.weibo.logic.entity.WeiboListParam

class WeiboViewModel : ViewModel(){
    private val paramLiveData = MutableLiveData<WeiboListParam>()

    val weiboList = ArrayList<Weibo>()

    val weiboListLiveData = Transformations.switchMap(paramLiveData){ param->
        Repository.getWeiboList(param)
    }

    fun getWeiboList(param : WeiboListParam){
        paramLiveData.value = param
    }
}