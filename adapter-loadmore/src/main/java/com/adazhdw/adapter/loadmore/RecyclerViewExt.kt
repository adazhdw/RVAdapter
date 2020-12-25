package com.adazhdw.adapter.loadmore

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IntDef
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * author：adazhdw
 * date-time：2020/12/23 9:50
 * description：RecyclerView 加载更多的扩展方法集
 **/

const val SCROLL_DIRECTION_TOP: Int = -1
const val SCROLL_DIRECTION_BOTTOM: Int = 1

@IntDef(SCROLL_DIRECTION_TOP, SCROLL_DIRECTION_BOTTOM)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ScrollDirection {}

@SuppressLint("ClickableViewAccessibility")
@JvmOverloads
fun RecyclerView.defaultLoadMoreListener(
    @ScrollDirection scrollDirection: Int = SCROLL_DIRECTION_BOTTOM,
    loadMoreAvailable: Boolean = true,//从RecyclerView onScroll中拦截 loadMore 是否可用
    onLoadMore: () -> Unit
) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            /*  state：
            SCROLL_STATE_IDLE     = 0 ：静止,没有滚动
            SCROLL_STATE_DRAGGING = 1 ：正在被外部拖拽,一般为用户正在用手指滚动
            SCROLL_STATE_SETTLING = 2 ：自动滚动开始

            RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部，手指往上滑
            RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部，手指往下滑
            */
            // 判断RecyclerView滚动到底部，参考：http://www.jianshu.com/p/c138055af5d2
            if (newState != RecyclerView.SCROLL_STATE_IDLE) return
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
            if (loadMoreAvailable && canLoadMore && newState == RecyclerView.SCROLL_STATE_IDLE && alreadyTopOrBottom()) {
                val adapter = adapter ?: return
                if (adapter is ILoadMore && adapter.loadMoreEnabled && !adapter.isLoading && !adapter.noMore) {
                    adapter.loading()
                    onLoadMore.invoke()
                }
            }
        }

        /**
         * RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部,手指往上滑
         */
        private fun isScrollingAlreadyBottom() = scrollDirection == SCROLL_DIRECTION_BOTTOM && !canScrollVertically(scrollDirection)

        /**
         * RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部,手指往下滑
         */
        private fun isScrollingAlreadyTop() = scrollDirection == SCROLL_DIRECTION_TOP && !canScrollVertically(scrollDirection)

        /**
         * 已滑动到顶部或者是底部
         */
        private fun alreadyTopOrBottom() = !canScrollVertically(scrollDirection)
    })
}

