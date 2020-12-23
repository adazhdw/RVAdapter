package com.adazhdw.adapter.loadmore

import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.adapter.core.DefaultViewHolder
import com.adazhdw.adapter.core.IItem

/**
 * FileName: DSL
 * Author: adazhdw
 * Date: 2020/12/23 17:51
 * Description:
 * History:
 */

fun loadMoreAdapter(
    adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    loadMoreItem: IItem<LoadMoreState, DefaultViewHolder> = LoadMoreItemDefault(),
    loadMoreEnabled: Boolean = true
) = LoadMoreAdapter(adapter, loadMoreItem, loadMoreEnabled)
