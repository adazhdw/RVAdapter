package com.adazhdw.adapter.list.ext

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * FileName: SwipeExt
 * Author: adazhdw
 * Date: 2020/12/30 19:30
 * Description: SwipeRefreshLayout 一些扩展方法
 * History:
 */

/**
 * 停止刷新ui
 */
fun SwipeRefreshLayout.startRefresh() {
    this.isRefreshing = true
}

/**
 * 启用刷新ui
 */
fun SwipeRefreshLayout.stopRefresh() {
    this.isRefreshing = false
}
