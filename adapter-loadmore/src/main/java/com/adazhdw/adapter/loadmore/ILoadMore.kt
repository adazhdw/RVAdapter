package com.adazhdw.adapter.loadmore

/**
 * author：adazhdw
 * date-time：2020/12/23 16:20
 * description：
 **/
interface ILoadMore {
    val isLoading: Boolean
    val loadMoreEnabled: Boolean
    val noMore: Boolean
    fun loading()
    fun loadComplete(error: Boolean = false, hasMore: Boolean = true)
}