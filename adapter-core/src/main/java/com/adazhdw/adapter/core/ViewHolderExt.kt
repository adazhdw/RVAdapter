@file:Suppress("UNCHECKED_CAST")

package com.adazhdw.adapter.core

import android.view.View
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

typealias ViewHolderType = DefaultViewHolder.() -> Unit
typealias ViewHolderBindView = DefaultViewHolder.(payloads: List<Any>) -> Unit


open class DefaultViewHolder(itemView: View) : BaseViewHolder(itemView), VHSubscriber {

    private var bindView: ViewHolderBindView? = null
    private var unBindType: ViewHolderType? = null
    private var attachType: ViewHolderType? = null
    private var detachType: ViewHolderType? = null

    fun onBindViewHolder(bindView: ViewHolderBindView) {
        this.bindView = bindView
    }

    fun unBindViewHolder(type: ViewHolderType) {
        this.unBindType = type
    }

    fun onViewAttachedToWindow(type: ViewHolderType) {
        this.attachType = type
    }

    fun onViewDetachedFromWindow(type: ViewHolderType) {
        this.detachType = type
    }

    override fun onBindViewHolder(position: Int, payloads: List<Any>) {
        this.bindView?.invoke(this, payloads)
    }

    override fun unBindViewHolder(position: Int) {
        this.unBindType?.invoke(this)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder, position: Int) {
        this.attachType?.invoke(holder as DefaultViewHolder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder, position: Int) {
        this.detachType?.invoke(holder as DefaultViewHolder)
    }
}


