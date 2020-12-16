package com.adazhdw.adapter.core

import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.rv.adapter.R

/**
 * author：daguozhu
 * date-time：2020/12/16 10:07
 * description：
 **/

inline fun <reified Adapter> RecyclerView.ViewHolder.getAdapter(): Adapter? {
    return itemView.getTag(R.id.adapter) as Adapter?
}

fun RecyclerView.ViewHolder.getRecyclerView(): RecyclerView? {
    return itemView.getTag(R.id.adapter_recyclerView) as RecyclerView?
}

inline fun <reified VI : ViewItem<*, *>> RecyclerView.ViewHolder.getViewItem(): VI? {
    return itemView.getTag(R.id.adapter_item) as VI?
}

internal typealias ViewHolderType = DefaultViewHolder.() -> Unit

