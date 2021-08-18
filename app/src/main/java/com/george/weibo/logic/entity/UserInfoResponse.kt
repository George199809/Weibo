package com.george.weibo.logic.entity

import com.google.gson.annotations.SerializedName

data class UserInfoResponse (
    @SerializedName("screen_name") val name : String,
    @SerializedName("location") val location : String,
    @SerializedName("profile_image_url") val profileUrl : String
)
