package com.adazhdw.adapter.loadmore

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * FileName: LoadMoreRecyclerView
 * Author: adazhdw
 * Date: 2020/12/25 17:14
 * Description:
 * History:
 */
class LoadMoreRecyclerView : RecyclerView {
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

    private var scrollOffset = 20f
    private var fingerUpward = false
    private var fingerLeftward = false
    private var lastX = 0f
    private var lastY = 0f
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (e != null)
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = e.x
                    lastY = e.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = e.x
                    val y = e.y
                    when {
                        lastY - y > scrollOffset -> {//手指向上划，View向上滚，滑动到底部
                            fingerUpward = true
                        }
                        y - lastY > scrollOffset -> {//手指向下划，View向下滚，滑动到顶部
                            fingerUpward = false
                        }
                        lastX - x > scrollOffset -> {//手指相左划，View向左滚动，滑动到最右边
                            fingerLeftward = true
                        }
                        x - lastX > scrollOffset -> {//手指向右划，View向右滚动，滑动到最左边
                            fingerLeftward = false
                        }
                    }
                }
            }
        Log.d(TAG, "fingerUpward:$fingerUpward,fingerLeftward:$fingerLeftward")
        return super.onTouchEvent(e)
    }

    @IntDef(SCROLL_DIRECTION_TOP, SCROLL_DIRECTION_BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    annotation class ScrollDirection {}

    private var isLoading = false//是否正在进行网络请求
    private var loadMoreAvailable = false//总开关，控制整体loadMore是否可用
    private var loadMoreEnabled = false//开关，控制ListFragment内loadMore是否可用
    private var mLoadMoreListener: LoadMoreListener? = null
    private var mScrollDirection: Int = SCROLL_DIRECTION_BOTTOM

    override fun onScrollStateChanged(state: Int) {
        /*  state：
            SCROLL_STATE_IDLE     = 0 ：静止,没有滚动
            SCROLL_STATE_DRAGGING = 1 ：正在被外部拖拽,一般为用户正在用手指滚动
            SCROLL_STATE_SETTLING = 2 ：自动滚动开始

            RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部，手指往上滑
            RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部，手指往下滑
            */
        // 判断RecyclerView滚动到底部，参考：http://www.jianshu.com/p/c138055af5d2
        if (state != SCROLL_STATE_IDLE) return
        val layoutManager = layoutManager
        val itemCount = layoutManager?.itemCount ?: 0
        if (itemCount <= 0) return
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
        if (loadMoreAvailable && canLoadMore && state == SCROLL_STATE_IDLE && alreadyTopOrBottom() && loadMoreEnabled && !isLoading) {
            val adapter = adapter ?: return
            if (adapter is ILoadMore && adapter.loadMoreEnabled && !adapter.isLoading && !adapter.noMore) {
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
        if (mScrollDirection == SCROLL_DIRECTION_BOTTOM && !canScrollVertically(mScrollDirection) && this.fingerUpward) {
            return true
        } else if (mScrollDirection == SCROLL_DIRECTION_TOP && !canScrollVertically(mScrollDirection) && !this.fingerUpward) {
            return true
        }
        return false
    }

    fun loadComplete() {
        this.isLoading = false
        this.loadMoreEnabled = true
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

    fun setScrollOffset(@IntRange(from = 0, to = 50) offset: Int) {
        this.scrollOffset = offset.toFloat()
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }

}