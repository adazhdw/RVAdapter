package com.adazhdw.adapter.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.adapter.core.GenericItem
import com.adazhdw.adapter.core.ListAdapter
import com.adazhdw.adapter.core.bind
import com.adazhdw.adapter.list.databinding.FragmentListLayoutRvexBinding
import com.adazhdw.adapter.list.decoration.LinearSpacingItemDecoration
import com.adazhdw.adapter.list.ext.startRefresh
import com.adazhdw.adapter.list.ext.stopRefresh
import com.adazhdw.adapter.loadmore.LoadMoreRecyclerView
import com.adazhdw.adapter.loadmore.LoadMoreRecyclerView.Companion.SCROLL_DIRECTION_BOTTOM
import com.adazhdw.ktlib.base.fragment.ViewBindingFragment

/**
 * FileName: ListFragment
 * Author: adazhdw
 * Date: 2020/12/25 10:41
 * Description: 自己封装的 直接使用的 ListFragment，只需要传入 Item
 */
abstract class ListFragmentEx<Item : GenericItem> : ViewBindingFragment<FragmentListLayoutRvexBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_list_layout_rvex
    private var currPage = 0
    private var isLoadingData = false
    protected val listAdapter: ListAdapter by lazy { ListAdapter() }
    protected val isRefreshing: Boolean
        get() = viewBinding.swipe.isRefreshing
    protected val mData: MutableList<GenericItem>
        get() = listAdapter.getData()

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentListLayoutRvexBinding {
        return FragmentListLayoutRvexBinding.inflate(inflater, container, false)
    }

    override fun initView(view: View) {
        viewBinding.swipe.isEnabled = refreshEnabled
        viewBinding.swipe.setOnRefreshListener { requestData(true) }
        viewBinding.dataRV.setLoadMoreAvailable(loadMoreAvailable())
        viewBinding.dataRV.addItemDecoration(itemDecoration())
        viewBinding.dataRV.canScrollDirection(SCROLL_DIRECTION_BOTTOM)
        listAdapter.bind(viewBinding.dataRV, getLayoutManager())
        viewBinding.dataRV.setLoadMoreListener(object : LoadMoreRecyclerView.LoadMoreListener {
            override fun onLoadMore() {
                requestData(false)
            }
        })
        rvExtra(viewBinding.dataRV)
    }

    override fun requestStart() {
        refresh()
    }

    fun refresh() {
        viewBinding.swipe.startRefresh()
        requestData(true)
    }

    private fun requestData(refreshing: Boolean) {
        if (!isLoadingData) {
            isLoadingData = true
            if (refreshing) {
                currPage = startAtPage()
                viewBinding.swipe.isEnabled = true
                viewBinding.dataRV.setLoadMoreEnabled(false)
            } else {
                viewBinding.swipe.isEnabled = false
                viewBinding.swipe.stopRefresh()
                viewBinding.dataRV.setLoadMoreEnabled(true)
            }
            onLoad(currPage, object : LoadDataCallback<Item> {
                override fun onSuccess(data: List<Item>, hasMore: Boolean) {
                    isLoadingData = false
                    if (refreshing) viewBinding.swipe.stopRefresh()
                    viewBinding.swipe.isEnabled = refreshEnabled
                    viewBinding.dataRV.loadComplete(error = false, hasMore = hasMore)
                    if (data.isNotEmpty()) currPage += 1
                    if (refreshing) {
                        listAdapter.setData(data)
                        if (data.isNotEmpty()) {
                            viewBinding.dataRV.scrollToPosition(0)
                        }
                    } else {
                        listAdapter.addData(data)
                    }
                }

                override fun onFail(code: Int, msg: String?) {
                    isLoadingData = false
                    if (refreshing) viewBinding.swipe.stopRefresh()
                    viewBinding.swipe.isEnabled = refreshEnabled
                    viewBinding.dataRV.loadComplete(error = true, hasMore = true)
                    onError(code, msg)
                }
            })
        }
    }

    abstract fun onLoad(page: Int, callback: LoadDataCallback<Item>)
    open var refreshEnabled: Boolean = true
    open fun getDataAdapter() = listAdapter
    open fun loadMoreAvailable(): Boolean = true/*总开关，控制loadMore是否可用*/
    open fun startAtPage() = 0/*开始页数*/
    open fun perPage() = 20/*每页个数pageSize*/
    open fun onError(code: Int, msg: String?) {}
    open fun rvExtra(recyclerView: LoadMoreRecyclerView) {}/*recyclerView其他属性设置*/
    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    open fun itemDecoration(): RecyclerView.ItemDecoration {
        return LinearSpacingItemDecoration(0, LinearLayoutManager.VERTICAL, true)
    }

    interface LoadDataCallback<Item> {
        fun onSuccess(data: List<Item>, hasMore: Boolean)
        fun onFail(code: Int, msg: String?)
    }

}