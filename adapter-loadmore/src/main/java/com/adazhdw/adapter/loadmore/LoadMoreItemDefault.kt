package com.adazhdw.adapter.loadmore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.adapter.core.AbsItem
import com.adazhdw.adapter.core.DefaultViewHolder

/**
 * author：adazhdw
 * date-time：2020/12/23 15:09
 * description：默认的 加载更多item实现
 **/

class LoadMoreItemDefault(override val layoutRes: Int = R.layout.adapter_item_load_more_layout) :
    AbsItem<LoadMoreState, RecyclerView.ViewHolder>() {
    init {
        data = LoadMoreState.Loaded
    }

    override fun getItemView(parent: ViewGroup, layoutInflater: LayoutInflater): View {
        return layoutInflater.inflate(layoutRes, parent, false)
    }

    override fun getViewHolder(view: View): RecyclerView.ViewHolder {
        return DefaultViewHolder(view)
    }

    override fun bindVH(holder: RecyclerView.ViewHolder, payloads: List<Any>) {
        if (holder is DefaultViewHolder) {
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

}

sealed class LoadMoreState {
    object Loading : LoadMoreState()
    object Loaded : LoadMoreState()
    object Error : LoadMoreState()
    object NoMore : LoadMoreState()
}