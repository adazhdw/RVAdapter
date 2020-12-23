package com.adazhdw.adapter.loadmore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.adapter.core.DefaultViewHolder
import com.adazhdw.adapter.core.IItem
import com.adazhdw.adapter.loadmore.wrapper.WrapperAdapter

/**
 * author：adazhdw
 * date-time：2020/12/23 15:08
 * description：
 **/
class LoadMoreAdapter(
    adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    private val loadMoreItem: IItem<LoadMoreState, DefaultViewHolder> = LoadMoreItemDefault(),
    override val loadMoreEnabled: Boolean = true
) : WrapperAdapter<RecyclerView.ViewHolder>(adapter), ILoadMore {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == loadMoreItem.itemViewType) {
            return loadMoreItem.getViewHolder(parent, LayoutInflater.from(parent.context))
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (isLoadMoreItem(position)) {
            loadMoreItem.bindVH(holder as DefaultViewHolder, payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isLoadMoreItem(position)) return loadMoreItem.itemViewType
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return getRealAdapter().itemCount + if (loadMoreEnabled) 1 else 0
    }

    private fun isLoadMoreItem(position: Int): Boolean {
        return position == getRealAdapter().itemCount
    }

    override val isLoading: Boolean
        get() = loadMoreItem.data == LoadMoreState.Loading

    override val noMore: Boolean
        get() = loadMoreItem.data == LoadMoreState.NoMore

    override fun loading() {
        loadMoreItem.data = LoadMoreState.Loading
        notifyItemChanged(getRealAdapter().itemCount)
    }

    override fun loadComplete(error: Boolean, hasMore: Boolean) {
        if (error) {
            loadMoreItem.data = LoadMoreState.Error
        } else {
            if (hasMore) {
                loadMoreItem.data = LoadMoreState.Loaded
            } else {
                loadMoreItem.data = LoadMoreState.NoMore
            }
        }
        notifyItemChanged(getRealAdapter().itemCount)
    }
}