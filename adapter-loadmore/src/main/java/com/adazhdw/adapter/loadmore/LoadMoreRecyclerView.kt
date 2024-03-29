package com.adazhdw.adapter.loadmore

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.IntDef
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.adazhdw.adapter.core.IItem

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
    private var fingerDirectionEnabled = false//是否启用手指滑动方向判断
    private var mLoadMoreListener: LoadMoreListener? = null
    private var mScrollDirection: Int = SCROLL_DIRECTION_BOTTOM

    private var initialTouchX = 0
    private var initialTouchY = 0
    private var lastTouchX = 0
    private var lastTouchY = 0
    private var scrollPointerId = -1
    private val scrollOffset = IntArray(2)
    private val reusableIntPair = IntArray(2)
    protected var fingerUp = false
    protected var fingerLeft = false
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (e == null) return super.onTouchEvent(e)
        if (fingerDirectionEnabled) {
            //使用RecyclerView部分源码来判断手指滑动方向
            val canScrollHorizontally = layoutManager?.canScrollHorizontally() ?: false
            val canScrollVertically = layoutManager?.canScrollVertically() ?: false
            val action = e.actionMasked
            val actionIndex = e.actionIndex
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    scrollPointerId = e.getPointerId(0)
                    initialTouchX = (e.x + 0.5f).toInt().also { lastTouchX = it }
                    initialTouchY = (e.y + 0.5f).toInt().also { lastTouchY = it }
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    scrollPointerId = e.getPointerId(actionIndex)
                    initialTouchX = (e.getX(actionIndex) + 0.5f).toInt().also { lastTouchX = it }
                    initialTouchY = (e.getY(actionIndex) + 0.5f).toInt().also { lastTouchY = it }
                }
                MotionEvent.ACTION_MOVE -> {
                    val index = e.findPointerIndex(scrollPointerId)
                    if (index < 0) return false

                    val x = (e.getX(index) + 0.5f).toInt()
                    val y = (e.getY(index) + 0.5f).toInt()
                    var dx = lastTouchX - x
                    var dy = lastTouchY - y
                    if (mLMScrollState != SCROLL_STATE_DRAGGING) {
                        var startScroll = false
                        if (canScrollHorizontally) {
                            dx = if (dx > 0) {
                                Math.max(0, dx)
                            } else {
                                Math.min(0, dx)
                            }
                            if (dx != 0) {
                                startScroll = true
                            }
                        }
                        if (canScrollVertically) {
                            dy = if (dy > 0) {
                                Math.max(0, dy)
                            } else {
                                Math.min(0, dy)
                            }
                            if (dy != 0) {
                                startScroll = true
                            }
                        }
                        if (startScroll) {
                            mLMScrollState = SCROLL_STATE_DRAGGING
                        }
                    }
                    if (mLMScrollState == SCROLL_STATE_DRAGGING) {
                        reusableIntPair[0] = 0
                        reusableIntPair[1] = 0
                        if (dispatchNestedPreScroll(
                                if (canScrollHorizontally) dx else 0, if (canScrollVertically) dy else 0,
                                reusableIntPair, scrollOffset, ViewCompat.TYPE_TOUCH
                            )
                        ) {
                            dx -= reusableIntPair[0]
                            dy -= reusableIntPair[1]
                            // Scroll has initiated, prevent parents from intercepting
                            parent.requestDisallowInterceptTouchEvent(true)
                        }
                        lastTouchX = x - scrollOffset[0]
                        lastTouchY = y - scrollOffset[1]
                    }
                    fingerUp = dy > 0
                    fingerLeft = dx > 0
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    onPointerUp(e)
                }
            }
        }
        return super.onTouchEvent(e)
    }

    private fun onPointerUp(e: MotionEvent) {
        val actionIndex = e.actionIndex
        if (e.getPointerId(actionIndex) == scrollPointerId) {
            // Pick a new pointer to pick up the slack.
            val newIndex = if (actionIndex == 0) 1 else 0
            scrollPointerId = e.getPointerId(newIndex)
            lastTouchX = (e.getX(newIndex) + 0.5f).toInt()
            initialTouchX = lastTouchX
            lastTouchY = (e.getY(newIndex) + 0.5f).toInt()
            initialTouchY = lastTouchY
        }
    }

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
        if (!canScrollVertically(mScrollDirection) && mScrollDirection == SCROLL_DIRECTION_BOTTOM && (if (fingerDirectionEnabled) fingerUp else true)) {
            already = true
        } else if (!canScrollVertically(mScrollDirection) && mScrollDirection == SCROLL_DIRECTION_TOP && (if (fingerDirectionEnabled) !fingerUp else true)) {
            already = true
        }
        return already
    }

    private var loadMoreAdapterEx: LoadMoreAdapter? = null
    private var loadMoreItem: IItem<LoadMoreState, ViewHolder> = LoadMoreItem()

    open fun loadComplete(error: Boolean = false, hasMore: Boolean = true) {
        this.isLoading = false
        this.loadMoreEnabled = true
        this.hasMore = hasMore
        this.loadMoreAdapterEx?.loadComplete(error, hasMore)
    }

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        if (adapter == null) return
        loadMoreAdapterEx = LoadMoreAdapter(adapter, loadMoreItem)
        super.setAdapter(loadMoreAdapterEx)
    }

    /**
     * 设置 loadmore 判断时是：已划到顶部（SCROLL_DIRECTION_TOP）或者底部（SCROLL_DIRECTION_BOTTOM）
     * SCROLL_DIRECTION_TOP 适用于 IM 中历史消息的加载
     */
    fun canScrollDirection(@ScrollDirection direction: Int) {
        this.mScrollDirection = direction
    }

    /**
     * 是否启用手指方向判，试验阶段，不建议启用
     */
    fun fingerDirectionEnabled(enabled: Boolean) {
        this.fingerDirectionEnabled = enabled
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