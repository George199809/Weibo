package com.george.weibo.logic.entity

import com.google.gson.annotations.SerializedName


data class WeiboListResponse (
    @SerializedName("statuses") val weiboList: MutableList<Weibo>
)

data class Weibo (
    @SerializedName("created_at") val createdTime: String,
    val user: User,
    val text: String,
    @SerializedName("pic_urls") val pics: MutableList<Pic>,
    @SerializedName("thumbnail_pic") val thumbnail_pic : String?,
    @SerializedName("bmiddle_pic") val bmiddle_pic : String?,
    @SerializedName("original_pic") val original_pic : String?
)

data class Pic(
    @SerializedName("thumbnail_pic") val thumbnail_pic : String,
    @SerializedName("bmiddle_pic") var bmiddle_pic : String?,
    @SerializedName("original_pic") var original_pic : String?
)

data class User(
    val id: Long,
    val name: String,
    @SerializedName("profile_image_url") val profileUrl: String
)