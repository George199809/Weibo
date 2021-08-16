package com.george.weibo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.george.weibo.logic.entity.WeiboListParam
import com.george.weibo.ui.WeiboViewModel

class MainActivity : AppCompatActivity() {
    val weiboViewModel by lazy { ViewModelProvider(this).get(WeiboViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.testBtn).setOnClickListener {
            weiboViewModel.getWeiboList(WeiboListParam(1,40))
        }

        weiboViewModel.weiboListLiveData.observe(this, Observer { result->
            val weiboList = result.getOrNull()
            println(weiboList)
        })
    }
}