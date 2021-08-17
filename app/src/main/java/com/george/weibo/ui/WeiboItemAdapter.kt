package com.george.weibo.ui

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
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
        holder.contextText.text = convertPlain2LinkText(mBlog.text)
        holder.contextText.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * input sample : "abc, #hello# george"
     * output sample : "abc <link>hello</hello> george"
     */
    fun convertPlain2LinkText(source : String) : CharSequence{
        var sourceType : Boolean = source[0]=='#'
        println(sourceType)
        val stringTokenizer = StringTokenizer(source, "#")

        var result : CharSequence = StringBuilder()
        var index : Int = 0;
        var previousType : Boolean = false // false-> plain text
        for(substring in stringTokenizer){
            var tmp : CharSequence
            index = source.indexOf(substring as String, index)
            var nowType : Boolean = previousType && index-2>=0 && source[index-2]=='#' || !previousType && index-1>=0 && source[index-1]=='#'
            previousType = nowType
            if(nowType){
                tmp = SpannableString(substring as CharSequence?)
                tmp.setSpan(object : ClickableSpan(){
                    override fun onClick(widget: View) {
                        Toast.makeText(WeiboApplication.context, "implementing", Toast.LENGTH_SHORT).show()
                    }
                }, 0, substring.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }else{
                tmp = substring as CharSequence
            }
            result = TextUtils.concat(result, tmp)
        }
        println(result)
        return result
    }
}