package com.adazhdw.adapter.core

import androidx.annotation.LayoutRes

/**
 * author：daguozhu
 * date-time：2020/12/16 9:56
 * description：
 **/

inline fun listAdapter(block: ListAdapter.() -> Unit): ListAdapter {
    return ListAdapter().apply { block() }
}

inline fun <M> defaultViewItem(
    @LayoutRes layoutRes: Int,
    model: M,
    crossinline bind: DefaultViewHolder.() -> Unit
) = DefaultViewItem<M>(layoutRes).apply {
    this.model = model
    onBindViewHolder {
        bind.invoke(this)
    }
}





