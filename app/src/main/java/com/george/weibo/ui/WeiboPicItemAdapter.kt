package com.george.weibo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.george.weibo.R
import com.george.weibo.WeiboApplication
import com.george.weibo.logic.entity.Weibo
import com.george.weibo.tools.LogUtils
import com.squareup.picasso.Picasso

class WeiboPicItemAdapter(val weibo: Weibo) : RecyclerView.Adapter<WeiboPicItemAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weiBoPicImg: ImageView = view.findViewById(R.id.weiboPicItemImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weibo_pic_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.weiBoPicImg.setOnClickListener {
            Toast.makeText(parent.context, "implementing", Toast.LENGTH_SHORT).show()
        }

        if (weibo.pics.isEmpty() || weibo.bmiddle_pic == null || weibo.original_pic == null) return viewHolder
        val bmiddleHeader = getUrlHeader(weibo.bmiddle_pic)
        val originalHeader = getUrlHeader(weibo.original_pic)
        for (pic in weibo.pics) {
            val picId = getPicId(pic.thumbnail_pic)
            pic.bmiddle_pic = bmiddleHeader + picId
            pic.original_pic = originalHeader + picId
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        LogUtils.d("WeiboPicItemAdapter", "pic url : ${weibo.pics[position].bmiddle_pic}")
        Glide.with(WeiboApplication.context).load(weibo.pics[position].bmiddle_pic).placeholder(R.drawable.ic_delete).into(holder.weiBoPicImg)
    }

    override fun getItemCount(): Int {
        return weibo.pics.size
    }

    private fun getPicId(url: String): String {
        var index = url.length - 1
        while (index > 0) {
            if (url[index] == '/')
                break;
            index--
        }

        return url.substring(index + 1, url.length)
    }

    private fun getUrlHeader(url: String): String {
        var index = url.length - 1
        while (index > 0) {
            if (url[index] == '/')
                break;
            index--
        }

        return url.substring(0, index + 1)
    }
}
