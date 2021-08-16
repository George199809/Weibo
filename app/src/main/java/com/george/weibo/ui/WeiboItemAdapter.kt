package com.george.weibo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.george.weibo.R
import com.george.weibo.WeiboApplication
import com.george.weibo.logic.entity.Weibo
import com.george.weibo.tools.MyDate
import com.squareup.picasso.Picasso
import java.util.*


class WeiboItemAdapter(val data: MutableList<Weibo>) : RecyclerView.Adapter<WeiboItemAdapter.ViewHolder>() {
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val userName : TextView = view.findViewById(R.id.userName)
        val profileImg : ImageView = view.findViewById(R.id.profileImg)
        val createdTime : TextView = view.findViewById(R.id.createdTime)
        val contextText : TextView = view.findViewById(R.id.contextTx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mblog_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.profileImg.setOnClickListener {
            Toast.makeText(parent.context, "hello", Toast.LENGTH_SHORT).show()
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mBlog = data[position]
        holder.userName.text = mBlog.user.name
        Picasso.with(WeiboApplication.context).load(mBlog.user.profileUrl).into(holder.profileImg);
        holder.createdTime.text = MyDate(Date(mBlog.createdTime)).toString()
        holder.contextText.text = mBlog.text
    }

    override fun getItemCount(): Int {
        return data.size
    }
}