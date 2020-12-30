package com.adazhdw.adapter.loadmore

import android.content.Context
import android.util.AttributeSet
import com.adazhdw.adapter.core.IItem

/**
 * FileName: LoadMoreRecyclerViewEx
 * Author: adazhdw
 * Date: 2020/12/25 17:14
 * Description: LoadMoreRecyclerView扩展类，封装了LoadMoreAdapter
 * History:
 */
class LoadMoreRecyclerViewEx : LoadMoreRecyclerView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //initViews
    }

    private var loadMoreAdapterEx: LoadMoreAdapter? = null
    private var loadMoreItem: IItem<LoadMoreState, ViewHolder> = LoadMoreItemDefault()

    override fun loadComplete(error: Boolean, hasMore: Boolean) {
        super.loadComplete(error, hasMore)
        this.loadMoreAdapterEx?.loadComplete(error, hasMore)
    }

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        if (adapter == null) return
        loadMoreAdapterEx = LoadMoreAdapter(adapter, loadMoreItem)
        super.setAdapter(loadMoreAdapterEx)
    }

}