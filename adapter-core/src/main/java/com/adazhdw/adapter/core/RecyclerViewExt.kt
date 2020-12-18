package com.adazhdw.adapter.core

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * author：adazhdw
 * date-time：2020/12/16 13:34
 * description：RecyclerView 相关的扩展方法
 **/

fun RecyclerView.Adapter<*>.bind(
    recyclerView: RecyclerView,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(recyclerView.context)
) = apply {
    recyclerView.layoutManager = layoutManager
    recyclerView.adapter = this
}

fun RecyclerView.isAlreadyBottom(): Boolean {
    return !canScrollVertically(1)
}

fun RecyclerView.isAlreadyTop(): Boolean {
    return canScrollVertically(-1)//的值表示是否能向下滚动，false表示已经滚动到顶部
}
