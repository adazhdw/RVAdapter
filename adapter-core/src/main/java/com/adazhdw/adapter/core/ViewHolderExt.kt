@file:Suppress("UNCHECKED_CAST")

package com.adazhdw.adapter.core

import androidx.recyclerview.widget.RecyclerView
import com.adazhdw.rv.adapter.R

/**
 * author：adazhdw
 * date-time：2020/12/18 13:20
 * description：
 **/


inline fun <reified Adapter> RecyclerView.ViewHolder.getAdapter(): Adapter? {
    return itemView.getTag(R.id.adapter) as Adapter?
}

fun RecyclerView.ViewHolder.getRecyclerView(): RecyclerView? {
    return itemView.getTag(R.id.adapter_recyclerView) as RecyclerView?
}

inline fun <reified VI : IItem<*, *>> RecyclerView.ViewHolder.getItem(): VI? {
    return itemView.getTag(R.id.adapter_item) as VI?
}

inline fun <reified M : Any> RecyclerView.ViewHolder.getItemData(): M? {
    return (itemView.getTag(R.id.adapter_item) as? IItem<M, *>)?.data
}

typealias ViewHolderType = DefaultViewHolder.(position: Int) -> Unit

typealias ViewHolderBindView = DefaultViewHolder.(position: Int, payloads: List<Any>) -> Unit


