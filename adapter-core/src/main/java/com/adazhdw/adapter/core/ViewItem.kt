package com.adazhdw.adapter.core

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * author：daguozhu
 * date-time：2020/12/15 18:10
 * description：用来管理 Adapter 实体类和 RecyclerView.ViewHolder 关系
 **/

typealias GenericViewItem = ViewItem<*, *>

interface ViewItem<M, VH : RecyclerView.ViewHolder> : ItemVHFactory<VH> {
    var model: M?
    var itemViewType: Int
        get() = layoutRes
        set(value) {}

    @get:LayoutRes
    val layoutRes: Int
    fun bindVH(holder: VH, payloads: List<Any>) {}
    fun unBindVH(holder: VH) {}
}
