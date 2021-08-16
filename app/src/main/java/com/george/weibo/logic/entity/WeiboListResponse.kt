package com.george.weibo.logic.entity

import com.google.gson.annotations.SerializedName


data class WeiboListResponse (
    @SerializedName("statuses") val weiboList: MutableList<Weibo>
)

data class Weibo (
    @SerializedName("created_at")
    val createdTime: String,
    val user: User,
    val text: String,
)

class User(
    val id: Long,
    val name: String,
    @SerializedName("profile_image_url")
    val profileUrl: String
)