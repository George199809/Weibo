package com.george.commontest.listern

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.george.weibo.WeiboApplication
import com.george.weibo.logic.entity.Weibo
import com.george.weibo.ui.WeiboItemAdapter
import com.george.weibo.ui.WeiboViewModel

class OnScrollListener(val layoutManager: LinearLayoutManager, val adapter: RecyclerView.Adapter<WeiboItemAdapter.ViewHolder>, val dataList: MutableList<Weibo>, val newData:MutableList<Weibo>) : RecyclerView.OnScrollListener() {


    var previousTotal = 0
    var loading = true
    val visibleThreshold = 10
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }

        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            val initialSize = dataList.size
            dataList.addAll(newData)
            val updatedSize = dataList.size
            recyclerView.post { adapter.notifyItemRangeInserted(initialSize, updatedSize) }
            loading = true
        }
    }
}