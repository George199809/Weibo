package com.george.weibo.ui

import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.george.weibo.R
import com.george.weibo.WebViewActivity
import com.george.weibo.WeiboApplication
import com.george.weibo.logic.entity.Weibo
import com.george.weibo.tools.LogUtils
import com.george.weibo.tools.MyDate
import java.util.*


class WeiboItemAdapter(val data: MutableList<Weibo>) : RecyclerView.Adapter<WeiboItemAdapter.ViewHolder>() {
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val userName : TextView = view.findViewById(R.id.userName)
        val profileImg : ImageView = view.findViewById(R.id.profileImg)
        val createdTime : TextView = view.findViewById(R.id.createdTime)
        val contextText : TextView = view.findViewById(R.id.contextTx)
        val picRecyclerView: RecyclerView = view.findViewById(R.id.picRecyclerView)
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
        Glide.with(WeiboApplication.context).load(mBlog.user.profileUrl).into(holder.profileImg)
        holder.createdTime.text = MyDate(Date(mBlog.createdTime)).toString()
        holder.contextText.text = convertPlain2LinkText(mBlog.text)
        holder.contextText.movementMethod = LinkMovementMethod.getInstance()
        var number = if(mBlog.pics.size < 3) mBlog.pics.size else 3
        if(number == 0) number = 1
        holder.picRecyclerView.layoutManager = GridLayoutManager(WeiboApplication.context, number, GridLayoutManager.VERTICAL, false)
        holder.picRecyclerView.adapter = WeiboPicItemAdapter(mBlog)
    }

    override fun getItemCount() = data.size

    /**
     * input sample : "abc, #hello# george http://abc"
     * output sample : "abc <link>hello</hello> george <link>前往</link>"
     */
    fun convertPlain2LinkText(_source : String) : CharSequence{
        // TODO 因为CharSequence使用replace会使得之前的setSpan失效。所以只能先去掉网页
        val regex = Regex("http://[^\\s]*")
        val find = regex.find(_source)
        LogUtils.d("convertPlain2LinkText", "find: ${find?.value}")
        var source = _source.replace(regex, "")
        LogUtils.d("convertPlain2LinkText", "source: ${source}")

        val stringTokenizer = StringTokenizer(source, "#")

        var result : CharSequence = StringBuilder()
        var index : Int = 0;
        var previousType : Boolean = false // false-> plain text
        for(substring in stringTokenizer){
            var tmp : CharSequence
            LogUtils.d("convertPlain2LinkText", "substring: ${substring}")
            index = source.indexOf(substring as String, index)
            LogUtils.d("convertPlain2LinkText", "index: ${index}")
            var nowType : Boolean = previousType && index-2>=0 && source[index-2]=='#' || !previousType && index-1>=0 && source[index-1]=='#'
            LogUtils.d("convertPlain2LinkText", "nowType: ${nowType}")
            previousType = nowType
            if(nowType){
                tmp = SpannableString(substring as CharSequence?)
                tmp.setSpan(object : ClickableSpan(){
                    override fun onClick(widget: View) {
                        val url : String = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=" + tmp.toString()
                        WebViewActivity.start(WeiboApplication.context, url)
                    }
                }, 0, substring.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }else{
                tmp = substring as CharSequence
            }
            index += substring.length+1         // TODO 如果把这行放在index = source.indexOf(substring as String, index) 会出现之前的链接失效
            result = TextUtils.concat(result, tmp)
        }

        LogUtils.d("convertPlain2LinkText", "result: ${result}")


        if(find?.value != null){
            var tmp : CharSequence = "前往"
            tmp = SpannableString(tmp as CharSequence?)
            tmp.setSpan(object : ClickableSpan(){
                override fun onClick(widget: View) {
                    val url : String = find.value
                    WebViewActivity.start(WeiboApplication.context, url)
                }
            }, 0, tmp.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            result = TextUtils.concat(result, tmp)
        }
        return result
    }
}