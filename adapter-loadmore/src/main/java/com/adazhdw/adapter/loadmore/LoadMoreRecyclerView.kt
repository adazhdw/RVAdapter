package com.adazhdw.adapter.loadmore

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.IntDef
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * FileName: LoadMoreRecyclerView
 * Author: adazhdw
 * Date: 2020/12/25 17:14
 * Description: 继承自RecyclerView的加载更多RecyclerView
 * History:
 */
open class LoadMoreRecyclerView : RecyclerView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //initViews
    }

    companion object {
        const val TAG = "LoadMoreRecyclerView"
        const val SCROLL_DIRECTION_TOP = -1
        const val SCROLL_DIRECTION_BOTTOM = 1
    }

    @IntDef(SCROLL_DIRECTION_TOP, SCROLL_DIRECTION_BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    annotation class ScrollDirection {}

    private var isLoading = false//是否正在进行网络请求
    private var loadMoreAvailable = false//总开关，控制整体loadMore是否可用
    private var loadMoreEnabled = false//开关，控制ListFragment内loadMore是否可用,为了防止刷新和加载更多同时进行
    private var hasMore = true//是否有更多数据
    private var mLoadMoreListener: LoadMoreListener? = null
    private var mScrollDirection: Int = SCROLL_DIRECTION_BOTTOM

    private var mLMScrollState = SCROLL_STATE_IDLE
    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        mLMScrollState = state
        /*  state：
            SCROLL_STATE_IDLE     = 0 ：静止,没有滚动
            SCROLL_STATE_DRAGGING = 1 ：正在被外部拖拽,一般为用户正在用手指滚动
            SCROLL_STATE_SETTLING = 2 ：自动滚动开始

            RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部，手指往上滑
            RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部，手指往下滑
            */
        // 判断RecyclerView滚动到底部，参考：http://www.jianshu.com/p/c138055af5d2
        if (mLMScrollState != SCROLL_STATE_IDLE) return
        val layoutManager = layoutManager
        val itemCount = layoutManager?.itemCount ?: 0
        val lastVisiblePosition: Int
        var canLoadMore = false
        when (layoutManager) {
            is GridLayoutManager -> {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (itemCount == lastVisiblePosition + 1) canLoadMore = true
            }
            is StaggeredGridLayoutManager -> {
                val into = intArrayOf(layoutManager.spanCount)
                layoutManager.findFirstVisibleItemPositions(into)
                lastVisiblePosition = into.max() ?: 0
                if (itemCount == lastVisiblePosition + 1) canLoadMore = true
            }
            is LinearLayoutManager -> {
                lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (itemCount == lastVisiblePosition + 1) canLoadMore = true
            }
        }
        if (loadMoreAvailable && canLoadMore && mLMScrollState == SCROLL_STATE_IDLE && alreadyTopOrBottom() && loadMoreEnabled && !isLoading && hasMore) {
            val adapter = adapter ?: return
            if (adapter is ILoadMore && !adapter.isLoading && !adapter.noMore) {
                adapter.loading()
                this.isLoading = true
                this.mLoadMoreListener?.onLoadMore()
            }
        }
    }

    /**
     * 1、手指向下划，并且View已经滚动到顶部，返回true
     * 2、手指向上划，并且View已经滑动到底部，返回true
     */
    private fun alreadyTopOrBottom(): Boolean {
        var already = false
        if (!canScrollVertically(mScrollDirection) && mScrollDirection == SCROLL_DIRECTION_BOTTOM/* && fingerUp*/) {
            already = true
        } else if (!canScrollVertically(mScrollDirection) && mScrollDirection == SCROLL_DIRECTION_TOP/* && !fingerUp*/) {
            already = true
        }
        return already
    }

    open fun loadComplete(error: Boolean = false, hasMore: Boolean = true) {
        this.isLoading = false
        this.loadMoreEnabled = true
        this.hasMore = hasMore
    }

    /**
     * 设置 loadmore 判断时是：已划到顶部（SCROLL_DIRECTION_TOP）或者底部（SCROLL_DIRECTION_BOTTOM）
     * SCROLL_DIRECTION_TOP 适用于 IM 中历史消息的加载
     */
    fun canScrollDirection(@ScrollDirection direction: Int) {
        this.mScrollDirection = direction
    }

    fun setLoadMoreAvailable(available: Boolean) {
        this.loadMoreAvailable = available
    }

    fun setLoadMoreEnabled(enabled: Boolean) {
        this.loadMoreEnabled = enabled
    }

    fun setLoadMoreListener(listener: LoadMoreListener) {
        this.mLoadMoreListener = listener
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }

}