package com.george.weibo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.george.weibo.logic.Repository
import com.george.weibo.logic.entity.UserInfoResponse

class UserInfoViewModel : ViewModel() {
    private val userIdLiveData = MutableLiveData<String>()

    val userInfo  = Transformations.switchMap(userIdLiveData){  uid->
        Repository.getUerInfo(uid)
    }

    fun getUser(uid : String){
        userIdLiveData.value = uid
    }
}