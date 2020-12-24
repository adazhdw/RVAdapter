package com.adazhdw.adapter.loadmore

import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.adapter.core.IItem
import com.adazhdw.adapter.loadmore.wrapper.WrapperAdapter

/**
 * author：adazhdw
 * date-time：2020/12/23 15:08
 * description：
 **/
class LoadMoreAdapter(
    adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    private val loadMoreItem: IItem<LoadMoreState, RecyclerView.ViewHolder> = LoadMoreItemDefault(),
    override val loadMoreEnabled: Boolean = true
) : WrapperAdapter<LoadMoreItemDefault, RecyclerView.ViewHolder>(adapter), ILoadMore {

    init {
        if (loadMoreEnabled) insertItems = listOf(loadMoreItem as LoadMoreItemDefault)
    }

    override fun getItemCount(): Int {
        return getRealAdapter().itemCount + if (loadMoreEnabled) 1 else 0
    }

    override fun shouldInsertItemAtPosition(position: Int): Boolean {
        return isLoadMoreItem(position)
    }

    override fun itemInsertedBeforeCount(position: Int): Int {
        if (isLoadMoreItem(position)) return 0
        return super.itemInsertedBeforeCount(position)
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