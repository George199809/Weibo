package com.george.weibo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.george.commontest.listern.OnScrollListener
import com.george.weibo.logic.entity.Weibo
import com.george.weibo.logic.entity.WeiboListParam
import com.george.weibo.ui.WeiboItemAdapter
import com.george.weibo.ui.WeiboViewModel

class MainActivity : AppCompatActivity() {
    val weiboViewModel by lazy { ViewModelProvider(this).get(WeiboViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
//        UETool.showUETMenu();
//
//        UETool.showUETMenu(0);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weiboViewModel.getWeiboList(WeiboListParam(1,20))


        val weiboRecyclerView = findViewById<RecyclerView>(R.id.weiboRecyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        val weiboItemAdapter = WeiboItemAdapter(weiboViewModel.weiboList)
        weiboRecyclerView.layoutManager = linearLayoutManager
        weiboRecyclerView.adapter = weiboItemAdapter
        findViewById<Button>(R.id.testBtn).setOnClickListener {
            weiboViewModel.getWeiboList(WeiboListParam(1,40))
        }

        weiboRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var previousTotal = 0
            var loading = true
            val visibleThreshold = 10
            var firstVisibleItem = 0
            var visibleItemCount = 0
            var totalItemCount = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = recyclerView.childCount
                totalItemCount = linearLayoutManager.itemCount
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }

                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    val initialSize = weiboViewModel.weiboList.size
                    weiboViewModel.getWeiboList(WeiboListParam(1,50))
                    Log.d("MainActivity","updata weibo list")
                    val updatedSize = weiboViewModel.weiboList.size
                    recyclerView.post { weiboItemAdapter.notifyItemRangeInserted(initialSize, updatedSize) }
                    loading = true
                }
            }
        })

        weiboViewModel.weiboListLiveData.observe(this, Observer { result ->
            val weiboList = result.getOrNull() as MutableList<Weibo>
            weiboViewModel.weiboList.addAll(weiboList)
            println(weiboList)
            weiboItemAdapter.notifyDataSetChanged()
        })

    }


}