package com.adazhdw.adapter.core

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Kotlin type alias to simplify usage for an all accepting item
 */
typealias GenericItem = IItem<*, out RecyclerView.ViewHolder>

/**
 * Created by mikepenz on 03.02.15.
 */
interface IItem<M : Any, VH : RecyclerView.ViewHolder> : IItemVHFactory<VH> {

    /** the item data Model*/
    var data: M?

    /** The type of the Item. Can be a hardcoded INT, but preferred is a defined id */
    var itemViewType: Int
        get() = layoutRes
        set(value) {}

    /** The layout for the given item */
    @get:LayoutRes
    val layoutRes: Int

    /** Binds the data of this item to the given holder */
    fun bindVH(holder: VH, payloads: List<Any>)

    /** View needs to release resources when its recycled */
    fun unbindVH(holder: VH)

    /** View got attached to the window */
    fun attachToWindow(holder: VH)

    /** View got detached from the window */
    fun detachFromWindow(holder: VH)
}
