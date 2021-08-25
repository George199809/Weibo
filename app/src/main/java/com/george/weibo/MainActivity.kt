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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.george.weibo.logic.entity.UserInfoResponse
import com.george.weibo.logic.entity.Weibo
import com.george.weibo.logic.entity.WeiboListParam
import com.george.weibo.tools.LogUtils
import com.george.weibo.ui.UserInfoViewModel
import com.george.weibo.ui.WeiboItemAdapter
import com.george.weibo.ui.WeiboViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class MainActivity : AppCompatActivity() {
    val weiboViewModel by lazy { ViewModelProvider(this).get(WeiboViewModel::class.java) }
    val userInfoViewModel by lazy { ViewModelProvider(this).get(UserInfoViewModel::class.java) }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> findViewById<DrawerLayout>(R.id.mainDrawerLayout).openDrawer(GravityCompat.START)
            R.id.hello -> Toast.makeText(this, "implementing", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        UETool.showUETMenu();
//        UETool.showUETMenu(0);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById<Toolbar>(R.id.mainToolBar))    // 需要放在setContentView之后
        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        weiboViewModel.getWeiboList()


        val weiboRecyclerView = findViewById<RecyclerView>(R.id.weiboRecyclerView)
        val gridLayoutManager = GridLayoutManager(this, 1)
        val weiboItemAdapter = WeiboItemAdapter(weiboViewModel.weiboList)
        weiboRecyclerView.layoutManager = gridLayoutManager
        weiboRecyclerView.adapter = weiboItemAdapter

        findViewById<SwipeRefreshLayout>(R.id.swipeRefresh).apply {
            setOnRefreshListener {
                Toast.makeText(this@MainActivity, "implementing", Toast.LENGTH_SHORT).show()
                this.isRefreshing = false
            }
        }


        // TODO 有没有办法将这里简化
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

        userInfoViewModel.getUser(WeiboApplication.UID)
        userInfoViewModel.userInfo.observe(this, Observer { result->
            val userInfoResponse = result.getOrNull() as UserInfoResponse
            LogUtils.d("MainActivity", "update userInfo ${userInfoResponse.name} + ${userInfoResponse.profileUrl}")
            val profileImg = findViewById<CircleImageView>(R.id.userProfileImg)
            Picasso.with(WeiboApplication.context).load(userInfoResponse.profileUrl).into(profileImg);
            findViewById<TextView>(R.id.userNameText).text = userInfoResponse.name
            findViewById<TextView>(R.id.LocationText).text = userInfoResponse.location
        })

        weiboViewModel.weiboListLiveData.observe(this, Observer { result ->
            val weiboList = result.getOrNull() as MutableList<Weibo>
            LogUtils.d("MainActivity","new weiboList size ${weiboList.size}")
            weiboViewModel.weiboList.addAll(weiboList)
            weiboItemAdapter.notifyDataSetChanged()
        })

    }




}