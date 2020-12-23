package com.adazhdw.adapter.loadmore

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.adazhdw.adapter.core.DefaultItem
import com.adazhdw.adapter.core.DefaultViewHolder

/**
 * author：adazhdw
 * date-time：2020/12/23 15:09
 * description：
 **/
class LoadMoreItemDefault : DefaultItem<LoadMoreState>(R.layout.adapter_item_load_more_layout) {
    init {
        data = LoadMoreState.Loaded
    }

    override fun bindVH(holder: DefaultViewHolder, payloads: List<Any>) {
        val text = holder.getView<TextView>(R.id.loadTv)
        val progress = holder.getView<ProgressBar>(R.id.progress)
        when (data) {
            LoadMoreState.Loading -> {
                text.visibility = View.INVISIBLE
                progress.visibility = View.VISIBLE
                text.text = "加载中"
            }
            LoadMoreState.Loaded -> {
                text.visibility = View.VISIBLE
                progress.visibility = View.INVISIBLE
                text.text = "加载成功"
            }
            LoadMoreState.Error -> {
                text.visibility = View.VISIBLE
                progress.visibility = View.INVISIBLE
                text.text = "加载错误"
            }
            LoadMoreState.NoMore -> {
                text.visibility = View.VISIBLE
                progress.visibility = View.INVISIBLE
                text.text = "无更多数据"
            }
        }
    }
}

sealed class LoadMoreState {
    object Loading : LoadMoreState()
    object Loaded : LoadMoreState()
    object Error : LoadMoreState()
    object NoMore : LoadMoreState()
}