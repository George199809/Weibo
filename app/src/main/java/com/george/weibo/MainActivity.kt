package com.george.weibo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.george.weibo.logic.entity.Weibo
import com.george.weibo.logic.entity.WeiboListParam
import com.george.weibo.ui.WeiboItemAdapter
import com.george.weibo.ui.WeiboViewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    val weiboViewModel by lazy { ViewModelProvider(this).get(WeiboViewModel::class.java) }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> findViewById<DrawerLayout>(R.id.mainDrawerLayout).openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        UETool.showUETMenu();
//        UETool.showUETMenu(0);

        super.onCreate(savedInstanceState)
//        supportActionBar?.hide()
        setSupportActionBar(findViewById<Toolbar>(R.id.mainToolBar))
        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        setContentView(R.layout.activity_main)
        weiboViewModel.getWeiboList()


        val weiboRecyclerView = findViewById<RecyclerView>(R.id.weiboRecyclerView)
//        val linearLayoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(this, 1)
        val weiboItemAdapter = WeiboItemAdapter(weiboViewModel.weiboList)
        weiboRecyclerView.layoutManager = gridLayoutManager
        weiboRecyclerView.adapter = weiboItemAdapter


        // TODO 有没有办法将这里简化
        // TODO 滚动逻辑有待优化，总数到达一定次数就无法滚动，初步判断是后端api的请求限制
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
                totalItemCount = gridLayoutManager.itemCount
                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }

                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    val initialSize = weiboViewModel.weiboList.size
                    weiboViewModel.getWeiboList()
                    Log.d("MainActivity","update weibo list")
                    val updatedSize = weiboViewModel.weiboList.size
                    if(initialSize == updatedSize)
                        Toast.makeText(WeiboApplication.context, "no new data", Toast.LENGTH_SHORT).show()
                    recyclerView.post { weiboItemAdapter.notifyItemRangeInserted(initialSize, updatedSize) }
                    loading = true
                }
            }
        })

        weiboViewModel.weiboListLiveData.observe(this, Observer { result ->
            val weiboList = result.getOrNull() as MutableList<Weibo>
            Log.d("MainActivity","new weiboList size ${weiboList.size}")
            weiboViewModel.weiboList.addAll(weiboList)
//            Log.d("MainActivity","weiboList ${weiboList}")
            weiboItemAdapter.notifyDataSetChanged()
        })

    }




}