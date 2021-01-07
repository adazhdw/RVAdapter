package com.adazhdw.adapter.loadmore

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * FileName: TouchDirectionRV
 * Author: adazhdw
 * Date: 2021/1/7 13:39
 * Description: 判断当前手指滑动方向：fingerUp,fingerLeft
 * History:
 */
class TouchDirectionRecyclerView : RecyclerView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //initViews
    }

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
                if (mScrollState != SCROLL_STATE_DRAGGING) {
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
                        mScrollState = SCROLL_STATE_DRAGGING
                    }
                }
                if (mScrollState == SCROLL_STATE_DRAGGING) {
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

    protected var mScrollState = SCROLL_STATE_IDLE
    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        mScrollState = state
    }
}